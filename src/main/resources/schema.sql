--
-- PostgreSQL database dump
--

\restrict qGgYpoQdIjrWWOlyHV46Qtj4jzW2SU2P23ZYVzei8eUVbPdEjIgLCnaLQC1GRLH

-- Dumped from database version 18.3 (Postgres.app)
-- Dumped by pg_dump version 18.2

-- Started on 2026-10-02 16:30:50 EEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 248 (class 1259 OID 16638)
-- Name: access_logs; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.access_logs (
                                    id bigint NOT NULL,
                                    user_email character varying(255),
                                    action character varying(255),
                                    page_url character varying(255),
                                    ip_address character varying(255),
                                    "timestamp" timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.access_logs OWNER TO razvaniacob;

--
-- TOC entry 247 (class 1259 OID 16637)
-- Name: access_logs_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.access_logs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.access_logs_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3975 (class 0 OID 0)
-- Dependencies: 247
-- Name: access_logs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.access_logs_id_seq OWNED BY public.access_logs.id;


--
-- TOC entry 228 (class 1259 OID 16445)
-- Name: addresses; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.addresses (
                                  id integer NOT NULL,
                                  user_id integer,
                                  city character varying(255),
                                  street character varying(255),
                                  is_default boolean DEFAULT false,
                                  zip_code character varying(255)
);


ALTER TABLE public.addresses OWNER TO razvaniacob;

--
-- TOC entry 227 (class 1259 OID 16444)
-- Name: addresses_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.addresses_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.addresses_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3976 (class 0 OID 0)
-- Dependencies: 227
-- Name: addresses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.addresses_id_seq OWNED BY public.addresses.id;


--
-- TOC entry 246 (class 1259 OID 16593)
-- Name: ai_quiz_logs; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.ai_quiz_logs (
                                     id bigint NOT NULL,
                                     user_id integer,
                                     quiz_data jsonb,
                                     recommended_product_id bigint,
                                     created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.ai_quiz_logs OWNER TO razvaniacob;

--
-- TOC entry 245 (class 1259 OID 16592)
-- Name: ai_quiz_logs_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.ai_quiz_logs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ai_quiz_logs_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3977 (class 0 OID 0)
-- Dependencies: 245
-- Name: ai_quiz_logs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.ai_quiz_logs_id_seq OWNED BY public.ai_quiz_logs.id;


--
-- TOC entry 220 (class 1259 OID 16392)
-- Name: categories; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.categories (
                                   id integer NOT NULL,
                                   name character varying(255) NOT NULL,
                                   description character varying(255)
);


ALTER TABLE public.categories OWNER TO razvaniacob;

--
-- TOC entry 219 (class 1259 OID 16391)
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.categories_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.categories_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3978 (class 0 OID 0)
-- Dependencies: 219
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.categories_id_seq OWNED BY public.categories.id;


--
-- TOC entry 232 (class 1259 OID 16476)
-- Name: order_items; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.order_items (
                                    id integer NOT NULL,
                                    order_id integer,
                                    product_id bigint,
                                    quantity integer NOT NULL,
                                    price_at_purchase numeric(38,2) NOT NULL
);


ALTER TABLE public.order_items OWNER TO razvaniacob;

--
-- TOC entry 231 (class 1259 OID 16475)
-- Name: order_items_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.order_items_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.order_items_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3979 (class 0 OID 0)
-- Dependencies: 231
-- Name: order_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.order_items_id_seq OWNED BY public.order_items.id;


--
-- TOC entry 230 (class 1259 OID 16461)
-- Name: orders; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.orders (
                               id integer NOT NULL,
                               user_id integer,
                               total_amount numeric(38,2),
                               status character varying(255) DEFAULT 'PENDING'::character varying,
                               created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                               shipping_address_id integer,
                               delivery_method character varying(255),
                               payment_method character varying(255)
);


ALTER TABLE public.orders OWNER TO razvaniacob;

--
-- TOC entry 229 (class 1259 OID 16460)
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.orders_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orders_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3980 (class 0 OID 0)
-- Dependencies: 229
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- TOC entry 250 (class 1259 OID 16809)
-- Name: payment_cards; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_cards (
                                      id integer NOT NULL,
                                      card_brand character varying(255),
                                      card_expiry character varying(255),
                                      card_holder character varying(255),
                                      card_number_last4 character varying(255),
                                      is_default boolean,
                                      user_id integer
);


ALTER TABLE public.payment_cards OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 16808)
-- Name: payment_cards_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.payment_cards ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.payment_cards_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    );


--
-- TOC entry 244 (class 1259 OID 16579)
-- Name: price_history; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.price_history (
                                      id integer NOT NULL,
                                      product_id bigint,
                                      old_price numeric(38,2),
                                      new_price numeric(38,2),
                                      change_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.price_history OWNER TO razvaniacob;

--
-- TOC entry 243 (class 1259 OID 16578)
-- Name: price_history_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.price_history_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.price_history_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3981 (class 0 OID 0)
-- Dependencies: 243
-- Name: price_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.price_history_id_seq OWNED BY public.price_history.id;


--
-- TOC entry 238 (class 1259 OID 16536)
-- Name: product_analytics; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.product_analytics (
                                          id bigint NOT NULL,
                                          product_id bigint,
                                          view_count integer DEFAULT 0,
                                          last_viewed_at timestamp without time zone,
                                          event_date date DEFAULT CURRENT_DATE
);


ALTER TABLE public.product_analytics OWNER TO razvaniacob;

--
-- TOC entry 237 (class 1259 OID 16535)
-- Name: product_analytics_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.product_analytics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.product_analytics_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3982 (class 0 OID 0)
-- Dependencies: 237
-- Name: product_analytics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.product_analytics_id_seq OWNED BY public.product_analytics.id;


--
-- TOC entry 240 (class 1259 OID 16551)
-- Name: product_discounts; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.product_discounts (
                                          id integer NOT NULL,
                                          product_id bigint,
                                          discount_percent integer,
                                          active_until timestamp without time zone
);


ALTER TABLE public.product_discounts OWNER TO razvaniacob;

--
-- TOC entry 239 (class 1259 OID 16550)
-- Name: product_discounts_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.product_discounts_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.product_discounts_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3983 (class 0 OID 0)
-- Dependencies: 239
-- Name: product_discounts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.product_discounts_id_seq OWNED BY public.product_discounts.id;


--
-- TOC entry 242 (class 1259 OID 16564)
-- Name: product_images; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.product_images (
                                       id integer NOT NULL,
                                       product_id bigint,
                                       image_url character varying(255)
);


ALTER TABLE public.product_images OWNER TO razvaniacob;

--
-- TOC entry 241 (class 1259 OID 16563)
-- Name: product_images_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.product_images_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.product_images_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3984 (class 0 OID 0)
-- Dependencies: 241
-- Name: product_images_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.product_images_id_seq OWNED BY public.product_images.id;


--
-- TOC entry 222 (class 1259 OID 16401)
-- Name: products; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.products (
                                 id bigint NOT NULL,
                                 name character varying(255) NOT NULL,
                                 brand character varying(255),
                                 price numeric(38,2) NOT NULL,
                                 stock_quantity integer DEFAULT 0,
                                 category_id integer,
                                 specs jsonb
);


ALTER TABLE public.products OWNER TO razvaniacob;

--
-- TOC entry 221 (class 1259 OID 16400)
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.products_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3985 (class 0 OID 0)
-- Dependencies: 221
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- TOC entry 234 (class 1259 OID 16496)
-- Name: reviews; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.reviews (
                                id integer NOT NULL,
                                product_id bigint,
                                user_id integer,
                                rating integer,
                                comment character varying(255),
                                created_at timestamp(6) without time zone,
                                CONSTRAINT reviews_rating_check CHECK (((rating >= 1) AND (rating <= 5)))
);


ALTER TABLE public.reviews OWNER TO razvaniacob;

--
-- TOC entry 233 (class 1259 OID 16495)
-- Name: reviews_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.reviews_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reviews_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3986 (class 0 OID 0)
-- Dependencies: 233
-- Name: reviews_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.reviews_id_seq OWNED BY public.reviews.id;


--
-- TOC entry 226 (class 1259 OID 16432)
-- Name: user_profiles; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.user_profiles (
                                      id integer NOT NULL,
                                      user_id integer,
                                      first_name character varying(255),
                                      last_name character varying(255),
                                      phone character varying(255)
);


ALTER TABLE public.user_profiles OWNER TO razvaniacob;

--
-- TOC entry 225 (class 1259 OID 16431)
-- Name: user_profiles_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.user_profiles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_profiles_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3987 (class 0 OID 0)
-- Dependencies: 225
-- Name: user_profiles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.user_profiles_id_seq OWNED BY public.user_profiles.id;


--
-- TOC entry 224 (class 1259 OID 16417)
-- Name: users; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.users (
                              id integer NOT NULL,
                              email character varying(255) NOT NULL,
                              password_hash character varying(255) NOT NULL,
                              role character varying(255) DEFAULT 'CLIENT'::character varying
);


ALTER TABLE public.users OWNER TO razvaniacob;

--
-- TOC entry 223 (class 1259 OID 16416)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3988 (class 0 OID 0)
-- Dependencies: 223
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 236 (class 1259 OID 16517)
-- Name: wishlist; Type: TABLE; Schema: public; Owner: razvaniacob
--

CREATE TABLE public.wishlist (
                                 id integer NOT NULL,
                                 user_id integer,
                                 product_id bigint,
                                 created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.wishlist OWNER TO razvaniacob;

--
-- TOC entry 235 (class 1259 OID 16516)
-- Name: wishlist_id_seq; Type: SEQUENCE; Schema: public; Owner: razvaniacob
--

CREATE SEQUENCE public.wishlist_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.wishlist_id_seq OWNER TO razvaniacob;

--
-- TOC entry 3989 (class 0 OID 0)
-- Dependencies: 235
-- Name: wishlist_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: razvaniacob
--

ALTER SEQUENCE public.wishlist_id_seq OWNED BY public.wishlist.id;


--
-- TOC entry 3769 (class 2604 OID 16641)
-- Name: access_logs id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.access_logs ALTER COLUMN id SET DEFAULT nextval('public.access_logs_id_seq'::regclass);


--
-- TOC entry 3751 (class 2604 OID 16448)
-- Name: addresses id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.addresses ALTER COLUMN id SET DEFAULT nextval('public.addresses_id_seq'::regclass);


--
-- TOC entry 3767 (class 2604 OID 16596)
-- Name: ai_quiz_logs id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.ai_quiz_logs ALTER COLUMN id SET DEFAULT nextval('public.ai_quiz_logs_id_seq'::regclass);


--
-- TOC entry 3745 (class 2604 OID 16395)
-- Name: categories id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.categories ALTER COLUMN id SET DEFAULT nextval('public.categories_id_seq'::regclass);


--
-- TOC entry 3756 (class 2604 OID 16479)
-- Name: order_items id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.order_items ALTER COLUMN id SET DEFAULT nextval('public.order_items_id_seq'::regclass);


--
-- TOC entry 3753 (class 2604 OID 16464)
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- TOC entry 3765 (class 2604 OID 16582)
-- Name: price_history id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.price_history ALTER COLUMN id SET DEFAULT nextval('public.price_history_id_seq'::regclass);


--
-- TOC entry 3760 (class 2604 OID 16539)
-- Name: product_analytics id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.product_analytics ALTER COLUMN id SET DEFAULT nextval('public.product_analytics_id_seq'::regclass);


--
-- TOC entry 3763 (class 2604 OID 16554)
-- Name: product_discounts id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.product_discounts ALTER COLUMN id SET DEFAULT nextval('public.product_discounts_id_seq'::regclass);


--
-- TOC entry 3764 (class 2604 OID 16567)
-- Name: product_images id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.product_images ALTER COLUMN id SET DEFAULT nextval('public.product_images_id_seq'::regclass);


--
-- TOC entry 3746 (class 2604 OID 16404)
-- Name: products id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- TOC entry 3757 (class 2604 OID 16499)
-- Name: reviews id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.reviews ALTER COLUMN id SET DEFAULT nextval('public.reviews_id_seq'::regclass);


--
-- TOC entry 3750 (class 2604 OID 16435)
-- Name: user_profiles id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.user_profiles ALTER COLUMN id SET DEFAULT nextval('public.user_profiles_id_seq'::regclass);


--
-- TOC entry 3748 (class 2604 OID 16420)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 3758 (class 2604 OID 16520)
-- Name: wishlist id; Type: DEFAULT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.wishlist ALTER COLUMN id SET DEFAULT nextval('public.wishlist_id_seq'::regclass);


--
-- TOC entry 3803 (class 2606 OID 16647)
-- Name: access_logs access_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.access_logs
    ADD CONSTRAINT access_logs_pkey PRIMARY KEY (id);


--
-- TOC entry 3783 (class 2606 OID 16454)
-- Name: addresses addresses_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.addresses
    ADD CONSTRAINT addresses_pkey PRIMARY KEY (id);


--
-- TOC entry 3801 (class 2606 OID 16602)
-- Name: ai_quiz_logs ai_quiz_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.ai_quiz_logs
    ADD CONSTRAINT ai_quiz_logs_pkey PRIMARY KEY (id);


--
-- TOC entry 3773 (class 2606 OID 16399)
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- TOC entry 3787 (class 2606 OID 16484)
-- Name: order_items order_items_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_pkey PRIMARY KEY (id);


--
-- TOC entry 3785 (class 2606 OID 16469)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 3805 (class 2606 OID 16816)
-- Name: payment_cards payment_cards_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_cards
    ADD CONSTRAINT payment_cards_pkey PRIMARY KEY (id);


--
-- TOC entry 3799 (class 2606 OID 16586)
-- Name: price_history price_history_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.price_history
    ADD CONSTRAINT price_history_pkey PRIMARY KEY (id);


--
-- TOC entry 3793 (class 2606 OID 16544)
-- Name: product_analytics product_analytics_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.product_analytics
    ADD CONSTRAINT product_analytics_pkey PRIMARY KEY (id);


--
-- TOC entry 3795 (class 2606 OID 16557)
-- Name: product_discounts product_discounts_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.product_discounts
    ADD CONSTRAINT product_discounts_pkey PRIMARY KEY (id);


--
-- TOC entry 3797 (class 2606 OID 16572)
-- Name: product_images product_images_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.product_images
    ADD CONSTRAINT product_images_pkey PRIMARY KEY (id);


--
-- TOC entry 3775 (class 2606 OID 16410)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- TOC entry 3789 (class 2606 OID 16505)
-- Name: reviews reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);


--
-- TOC entry 3781 (class 2606 OID 16438)
-- Name: user_profiles user_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_pkey PRIMARY KEY (id);


--
-- TOC entry 3777 (class 2606 OID 16430)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 3779 (class 2606 OID 16428)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3791 (class 2606 OID 16524)
-- Name: wishlist wishlist_pkey; Type: CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.wishlist
    ADD CONSTRAINT wishlist_pkey PRIMARY KEY (id);


--
-- TOC entry 3808 (class 2606 OID 16455)
-- Name: addresses addresses_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.addresses
    ADD CONSTRAINT addresses_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3821 (class 2606 OID 16603)
-- Name: ai_quiz_logs ai_quiz_logs_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.ai_quiz_logs
    ADD CONSTRAINT ai_quiz_logs_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3822 (class 2606 OID 16817)
-- Name: payment_cards fkbs8w6tjl44mr8778hwv1orct5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_cards
    ADD CONSTRAINT fkbs8w6tjl44mr8778hwv1orct5 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3809 (class 2606 OID 16801)
-- Name: orders fkmk6q95x8ffidq82wlqjaq7sqc; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fkmk6q95x8ffidq82wlqjaq7sqc FOREIGN KEY (shipping_address_id) REFERENCES public.addresses(id);


--
-- TOC entry 3811 (class 2606 OID 16485)
-- Name: order_items order_items_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(id) ON DELETE CASCADE;


--
-- TOC entry 3812 (class 2606 OID 16490)
-- Name: order_items order_items_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3810 (class 2606 OID 16470)
-- Name: orders orders_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3820 (class 2606 OID 16587)
-- Name: price_history price_history_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.price_history
    ADD CONSTRAINT price_history_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3817 (class 2606 OID 16655)
-- Name: product_analytics product_analytics_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.product_analytics
    ADD CONSTRAINT product_analytics_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id) ON DELETE CASCADE;


--
-- TOC entry 3818 (class 2606 OID 16558)
-- Name: product_discounts product_discounts_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.product_discounts
    ADD CONSTRAINT product_discounts_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3819 (class 2606 OID 16573)
-- Name: product_images product_images_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.product_images
    ADD CONSTRAINT product_images_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3806 (class 2606 OID 16411)
-- Name: products products_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.categories(id);


--
-- TOC entry 3813 (class 2606 OID 16506)
-- Name: reviews reviews_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3814 (class 2606 OID 16511)
-- Name: reviews reviews_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3807 (class 2606 OID 16439)
-- Name: user_profiles user_profiles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 3815 (class 2606 OID 16530)
-- Name: wishlist wishlist_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.wishlist
    ADD CONSTRAINT wishlist_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3816 (class 2606 OID 16525)
-- Name: wishlist wishlist_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: razvaniacob
--

ALTER TABLE ONLY public.wishlist
    ADD CONSTRAINT wishlist_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


-- Completed on 2026-10-02 16:30:50 EEST

--
-- PostgreSQL database dump complete
--

\unrestrict qGgYpoQdIjrWWOlyHV46Qtj4jzW2SU2P23ZYVzei8eUVbPdEjIgLCnaLQC1GRLH

