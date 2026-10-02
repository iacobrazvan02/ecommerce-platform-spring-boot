"""
Predicție ML pentru Best-Seller 2026 — ElectroTech Store
Algoritm: Random Forest Regressor

Ideea:
  Colectez date reale din magazin (vizualizari, vanzari, rating-uri, wishlist-uri)
  și am antrenat un model de Machine Learning care invata ce face un produs popular.
  Apoi simulez o creștere de 20% în vizualizări pentru anul următor
  și prezicez care produs va fi cel mai cerut.

Feature-uri (X):
  1. view_count        – de cate ori a fost vizualizat produsul
  2. times_ordered     – de cate ori a fost comandat
  3. total_qty_sold    – cate bucați s-au vandut în total
  4. avg_rating        – nota medie din review-uri (1-5)
  5. review_count      – cate review-uri are
  6. wishlist_count    – de cați useri e pus în wishlist
  7. price             – prețul produsului
  8. stock_quantity    – câte bucați sunt în stoc

Target (Y):
  popularity_score = vânzări + vizualizări/50 + wishlist*2 + rating_avg*review_count
  (un scor real care combină toate semnalele de popularitate)
"""

import psycopg2
import pandas as pd
import numpy as np
import warnings
warnings.filterwarnings("ignore")

from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import StandardScaler

DB_CONFIG = {
    "dbname": "electrotech_db",
    "user": "postgres",
    "password": "",
    "host": "localhost",
    "port": "5432"
}


def get_prediction():
    try:
        conn = psycopg2.connect(**DB_CONFIG)

        query = """
            SELECT
                p.id,
                p.name,
                p.price,
                p.stock_quantity,
                COALESCE(pa.view_count, 0)                          AS view_count,
                COALESCE(sales.times_ordered, 0)                    AS times_ordered,
                COALESCE(sales.total_qty_sold, 0)                   AS total_qty_sold,
                COALESCE(rev.avg_rating, 0)                         AS avg_rating,
                COALESCE(rev.review_count, 0)                       AS review_count,
                COALESCE(wl.wishlist_count, 0)                      AS wishlist_count
            FROM products p
            LEFT JOIN product_analytics pa
                ON p.id = pa.product_id
            LEFT JOIN (
                SELECT oi.product_id,
                       COUNT(DISTINCT oi.order_id)  AS times_ordered,
                       SUM(oi.quantity)              AS total_qty_sold
                FROM order_items oi
                JOIN orders o ON oi.order_id = o.id
                WHERE o.status IN ('COMPLETED', 'PENDING')
                GROUP BY oi.product_id
            ) sales ON p.id = sales.product_id
            LEFT JOIN (
                SELECT product_id,
                       AVG(rating)::numeric(3,1)    AS avg_rating,
                       COUNT(id)                    AS review_count
                FROM reviews
                GROUP BY product_id
            ) rev ON p.id = rev.product_id
            LEFT JOIN (
                SELECT product_id,
                       COUNT(id)                    AS wishlist_count
                FROM wishlist
                GROUP BY product_id
            ) wl ON p.id = wl.product_id
        """
        df = pd.read_sql(query, conn)
        conn.close()

        if df.empty or len(df) < 5:
            print("Date insuficiente pentru predicție")
            return

        df['popularity_score'] = (
            df['total_qty_sold'] * 5
            + df['view_count'] / 50
            + df['wishlist_count'] * 3
            + df['avg_rating'] * df['review_count']
        )

        features = [
            'view_count', 'times_ordered', 'total_qty_sold',
            'avg_rating', 'review_count', 'wishlist_count',
            'price', 'stock_quantity'
        ]
        X = df[features].copy()
        y = df['popularity_score']
        scaler = StandardScaler()
        X_scaled = scaler.fit_transform(X)

        model = RandomForestRegressor(
            n_estimators=100,
            max_depth=6,
            random_state=42
        )
        model.fit(X_scaled, y)

        X_future = df[features].copy()
        X_future['view_count']     = X_future['view_count']     * 1.20
        X_future['times_ordered']  = X_future['times_ordered']  * 1.15
        X_future['total_qty_sold'] = X_future['total_qty_sold'] * 1.15
        X_future['wishlist_count'] = X_future['wishlist_count'] * 1.25

        X_future_scaled = scaler.transform(X_future)
        df['predicted_score_2026'] = model.predict(X_future_scaled)

        import json
        top3 = df.nlargest(3, 'predicted_score_2026')
        result = []
        for _, row in top3.iterrows():
            result.append(row['name'])
        print(json.dumps(result))

    except Exception as e:
        print(f"Eroare: {e}")


if __name__ == "__main__":
    get_prediction()