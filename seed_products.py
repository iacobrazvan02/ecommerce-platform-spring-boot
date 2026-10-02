import psycopg2
import json

DB_CONFIG = {
    "dbname": "electrotech_db",
    "user": "postgres",
    "password": "",
    "host": "localhost",
    "port": "5432"
}

CATEGORIES = [
    ("Smartphone", "Telefoane mobile de ultimă generație"),
    ("Laptopuri", "Laptopuri performante pentru orice nevoie"),
    ("Tablete", "Tablete performante pentru productivitate și divertisment"),
    ("TV", "Televizoare Smart cu rezoluție 4K și 8K"),
    ("Electrocasnice mari", "Mașini de spălat, frigidere, aragaze"),
    ("Electrocasnice mici", "Cuptoare cu microunde, aspiratoare, blendere"),
    ("Gaming", "Console, periferice și accesorii gaming"),
    ("Accesorii", "Căști, ceasuri, încărcătoare și altele"),
]


PRODUCTS = [
    # ==================== SMARTPHONE ====================
    ("iPhone 16 Pro Max", "Apple", "Smartphone", 8499.99, 12,
     {"Ecran": "6.9 inch Super Retina XDR OLED", "Procesor": "A18 Pro", "RAM": "8GB", "Stocare": "256GB", "Camera": "48MP + 12MP + 12MP", "Baterie": "4685 mAh", "5G": "Da"},
     ["https://i.imgur.com/QxEjZrS.png"]),

    ("iPhone 16 Pro", "Apple", "Smartphone", 7299.99, 15,
     {"Ecran": "6.3 inch Super Retina XDR OLED", "Procesor": "A18 Pro", "RAM": "8GB", "Stocare": "256GB", "Camera": "48MP + 12MP + 12MP", "Baterie": "3582 mAh", "5G": "Da"},
     ["https://i.imgur.com/LkqZrSP.png"]),

    ("iPhone 15", "Apple", "Smartphone", 4499.99, 20,
     {"Ecran": "6.1 inch Super Retina XDR OLED", "Procesor": "A16 Bionic", "RAM": "6GB", "Stocare": "128GB", "Camera": "48MP + 12MP", "Baterie": "3349 mAh", "5G": "Da"},
     ["https://i.imgur.com/YhVJfmN.png"]),

    ("Samsung Galaxy S24 Ultra", "Samsung", "Smartphone", 7499.99, 10,
     {"Ecran": "6.8 inch Dynamic AMOLED 2X", "Procesor": "Snapdragon 8 Gen 3", "RAM": "12GB", "Stocare": "256GB", "Camera": "200MP + 12MP + 50MP + 10MP", "Baterie": "5000 mAh", "S Pen": "Inclus"},
     ["https://i.imgur.com/8nKZqVx.png"]),

    ("Samsung Galaxy S24", "Samsung", "Smartphone", 4799.99, 18,
     {"Ecran": "6.2 inch Dynamic AMOLED 2X", "Procesor": "Exynos 2400", "RAM": "8GB", "Stocare": "128GB", "Camera": "50MP + 12MP + 10MP", "Baterie": "4000 mAh", "5G": "Da"},
     ["https://i.imgur.com/mVrQZwx.png"]),

    ("Samsung Galaxy A55", "Samsung", "Smartphone", 2199.99, 25,
     {"Ecran": "6.6 inch Super AMOLED", "Procesor": "Exynos 1480", "RAM": "8GB", "Stocare": "128GB", "Camera": "50MP + 12MP + 5MP", "Baterie": "5000 mAh", "5G": "Da"},
     ["https://i.imgur.com/pLZJfmN.png"]),

    ("Google Pixel 9 Pro", "Google", "Smartphone", 5999.99, 8,
     {"Ecran": "6.3 inch LTPO OLED", "Procesor": "Google Tensor G4", "RAM": "16GB", "Stocare": "128GB", "Camera": "50MP + 48MP + 48MP", "Baterie": "4700 mAh", "AI": "Gemini Nano"},
     ["https://i.imgur.com/kZqVxmR.png"]),

    ("Google Pixel 8a", "Google", "Smartphone", 2799.99, 14,
     {"Ecran": "6.1 inch OLED", "Procesor": "Google Tensor G3", "RAM": "8GB", "Stocare": "128GB", "Camera": "64MP + 13MP", "Baterie": "4492 mAh", "AI": "Da"},
     ["https://i.imgur.com/xZqVkmR.png"]),

    ("Xiaomi 14", "Xiaomi", "Smartphone", 4299.99, 16,
     {"Ecran": "6.36 inch LTPO AMOLED", "Procesor": "Snapdragon 8 Gen 3", "RAM": "12GB", "Stocare": "256GB", "Camera": "50MP Leica + 50MP + 50MP", "Baterie": "4610 mAh", "Încărcare": "90W"},
     ["https://i.imgur.com/RZqVxmk.png"]),

    ("Xiaomi Redmi Note 13 Pro", "Xiaomi", "Smartphone", 1499.99, 30,
     {"Ecran": "6.67 inch AMOLED", "Procesor": "Snapdragon 7s Gen 2", "RAM": "8GB", "Stocare": "256GB", "Camera": "200MP + 8MP + 2MP", "Baterie": "5100 mAh", "Încărcare": "67W"},
     ["https://i.imgur.com/VxmRZqk.png"]),

    ("OnePlus 12", "OnePlus", "Smartphone", 4999.99, 11,
     {"Ecran": "6.82 inch LTPO AMOLED", "Procesor": "Snapdragon 8 Gen 3", "RAM": "12GB", "Stocare": "256GB", "Camera": "50MP + 48MP + 64MP", "Baterie": "5400 mAh", "Încărcare": "100W SUPERVOOC"},
     ["https://i.imgur.com/qVxmRZk.png"]),

    ("Motorola Edge 50 Pro", "Motorola", "Smartphone", 3299.99, 13,
     {"Ecran": "6.7 inch pOLED", "Procesor": "Snapdragon 7 Gen 3", "RAM": "12GB", "Stocare": "256GB", "Camera": "50MP + 13MP + 10MP", "Baterie": "4500 mAh", "Încărcare": "125W TurboPower"},
     ["https://i.imgur.com/xmRZqVk.png"]),


    ("MacBook Pro 16 M3 Pro", "Apple", "Laptopuri", 14999.99, 6,
     {"Ecran": "16.2 inch Liquid Retina XDR", "Procesor": "Apple M3 Pro", "RAM": "18GB", "SSD": "512GB", "GPU": "M3 Pro 14-core", "Baterie": "22 ore", "Greutate": "2.14 kg"},
     ["https://i.imgur.com/ZRmVxqk.png"]),

    ("MacBook Air 15 M3", "Apple", "Laptopuri", 8499.99, 10,
     {"Ecran": "15.3 inch Liquid Retina", "Procesor": "Apple M3", "RAM": "8GB", "SSD": "256GB", "GPU": "M3 10-core", "Baterie": "18 ore", "Greutate": "1.51 kg"},
     ["https://i.imgur.com/RmVxqZk.png"]),

    ("ASUS ROG Strix G16", "ASUS", "Laptopuri", 7999.99, 8,
     {"Ecran": "16 inch FHD+ 165Hz", "Procesor": "Intel Core i7-13650HX", "RAM": "16GB DDR5", "SSD": "512GB", "GPU": "NVIDIA RTX 4060 8GB", "Baterie": "90Wh", "Tastatură": "RGB per-key"},
     ["https://i.imgur.com/mVxqZRk.png"]),

    ("ASUS ZenBook 14 OLED", "ASUS", "Laptopuri", 5499.99, 12,
     {"Ecran": "14 inch 2.8K OLED 90Hz", "Procesor": "Intel Core Ultra 7 155H", "RAM": "16GB LPDDR5X", "SSD": "512GB", "GPU": "Intel Arc", "Baterie": "75Wh", "Greutate": "1.28 kg"},
     ["https://i.imgur.com/VxqZRmk.png"]),

    ("Lenovo ThinkPad X1 Carbon Gen 12", "Lenovo", "Laptopuri", 9999.99, 5,
     {"Ecran": "14 inch 2.8K OLED", "Procesor": "Intel Core Ultra 7 165U", "RAM": "32GB LPDDR5X", "SSD": "1TB", "Greutate": "1.09 kg", "Securitate": "Fingerprint + IR Camera", "MIL-STD": "810H"},
     ["https://i.imgur.com/xqZRmVk.png"]),

    ("Lenovo IdeaPad 5 Pro", "Lenovo", "Laptopuri", 3999.99, 15,
     {"Ecran": "16 inch 2.5K IPS 120Hz", "Procesor": "AMD Ryzen 7 7735HS", "RAM": "16GB DDR5", "SSD": "512GB", "GPU": "AMD Radeon 680M", "Baterie": "75Wh", "Greutate": "1.89 kg"},
     ["https://i.imgur.com/qZRmVxk.png"]),

    ("HP Spectre x360 16", "HP", "Laptopuri", 10499.99, 4,
     {"Ecran": "16 inch 3K+ OLED Touch", "Procesor": "Intel Core Ultra 7 155H", "RAM": "32GB LPDDR5X", "SSD": "1TB", "GPU": "Intel Arc", "Convertibil": "360°", "Stylus": "Inclus"},
     ["https://i.imgur.com/ZRmVkxq.png"]),

    ("HP Pavilion 15", "HP", "Laptopuri", 2999.99, 20,
     {"Ecran": "15.6 inch FHD IPS", "Procesor": "Intel Core i5-1335U", "RAM": "8GB DDR4", "SSD": "512GB", "GPU": "Intel Iris Xe", "Baterie": "41Wh", "OS": "Windows 11"},
     ["https://i.imgur.com/RmVkxqZ.png"]),

    ("Dell XPS 15", "Dell", "Laptopuri", 8999.99, 7,
     {"Ecran": "15.6 inch 3.5K OLED", "Procesor": "Intel Core i7-13700H", "RAM": "16GB DDR5", "SSD": "512GB", "GPU": "NVIDIA RTX 4050 6GB", "Greutate": "1.86 kg", "Thunderbolt": "4"},
     ["https://i.imgur.com/mVkxqZR.png"]),

    ("Dell Inspiron 16", "Dell", "Laptopuri", 3499.99, 18,
     {"Ecran": "16 inch FHD+ IPS", "Procesor": "AMD Ryzen 5 7530U", "RAM": "8GB DDR4", "SSD": "512GB", "GPU": "AMD Radeon", "Baterie": "54Wh", "OS": "Windows 11"},
     ["https://i.imgur.com/VkxqZRm.png"]),

    ("Acer Nitro V 15", "Acer", "Laptopuri", 4499.99, 14,
     {"Ecran": "15.6 inch FHD IPS 144Hz", "Procesor": "Intel Core i5-13420H", "RAM": "16GB DDR5", "SSD": "512GB", "GPU": "NVIDIA RTX 4050 6GB", "Răcire": "Dual-fan", "Tastatură": "RGB"},
     ["https://i.imgur.com/kxqZRmV.png"]),


    ("Samsung Neo QLED 55QN85D", "Samsung", "TV", 5999.99, 6,
     {"Diagonală": "55 inch", "Rezoluție": "4K UHD", "Panou": "Neo QLED Mini LED", "HDR": "HDR10+ / HLG", "Procesor": "NQ4 AI Gen2", "Smart TV": "Tizen", "Refresh": "120Hz"},
     ["https://i.imgur.com/xqRZmVk.png"]),

    ("Samsung Crystal UHD 50CU7172", "Samsung", "TV", 1999.99, 15,
     {"Diagonală": "50 inch", "Rezoluție": "4K UHD", "Panou": "Crystal UHD", "HDR": "HDR10+ / HLG", "Procesor": "Crystal 4K", "Smart TV": "Tizen", "Refresh": "60Hz"},
     ["https://i.imgur.com/qRZmVkx.png"]),

    ("LG OLED55C4", "LG", "TV", 6499.99, 5,
     {"Diagonală": "55 inch", "Rezoluție": "4K UHD", "Panou": "OLED evo", "HDR": "Dolby Vision / HDR10 / HLG", "Procesor": "α9 Gen7 AI", "Smart TV": "webOS 24", "Refresh": "120Hz"},
     ["https://i.imgur.com/RZmVkxq.png"]),

    ("LG NanoCell 50NANO82T6B", "LG", "TV", 2499.99, 12,
     {"Diagonală": "50 inch", "Rezoluție": "4K UHD", "Panou": "NanoCell", "HDR": "HDR10 / HLG", "Procesor": "α5 Gen7 AI", "Smart TV": "webOS 24", "Refresh": "60Hz"},
     ["https://i.imgur.com/ZmVkxqR.png"]),

    ("Sony Bravia XR-55A80L", "Sony", "TV", 7499.99, 4,
     {"Diagonală": "55 inch", "Rezoluție": "4K UHD", "Panou": "OLED", "HDR": "Dolby Vision / HDR10 / HLG", "Procesor": "XR Cognitive", "Smart TV": "Google TV", "Sunet": "Acoustic Surface Audio+"},
     ["https://i.imgur.com/mVkxRqZ.png"]),

    ("Sony Bravia KD-55X80L", "Sony", "TV", 3499.99, 10,
     {"Diagonală": "55 inch", "Rezoluție": "4K UHD", "Panou": "LED", "HDR": "HDR10 / HLG", "Procesor": "X1 4K HDR", "Smart TV": "Google TV", "Triluminos": "Pro"},
     ["https://i.imgur.com/VkxRqZm.png"]),

    ("Philips 55PUS8808 Ambilight", "Philips", "TV", 3999.99, 8,
     {"Diagonală": "55 inch", "Rezoluție": "4K UHD", "Panou": "LED", "HDR": "Dolby Vision / HDR10+ / HLG", "Procesor": "P5 Perfect Picture", "Ambilight": "3 laturi", "Smart TV": "Google TV"},
     ["https://i.imgur.com/kxRqZmV.png"]),

    ("TCL 55C835 Mini LED", "TCL", "TV", 2999.99, 11,
     {"Diagonală": "55 inch", "Rezoluție": "4K UHD", "Panou": "Mini LED QLED", "HDR": "Dolby Vision / HDR10+", "Refresh": "144Hz", "Smart TV": "Google TV", "Sunet": "Onkyo 2.1"},
     ["https://i.imgur.com/xRqZmVk.png"]),

    ("Hisense 55U7KQ", "Hisense", "TV", 2599.99, 13,
     {"Diagonală": "55 inch", "Rezoluție": "4K UHD", "Panou": "ULED Mini LED", "HDR": "Dolby Vision / HDR10+", "Refresh": "144Hz", "Smart TV": "VIDAA U7", "Gaming": "ALLM + VRR"},
     ["https://i.imgur.com/RqZmVkx.png"]),

    ("Samsung The Frame 55LS03B", "Samsung", "TV", 4999.99, 6,
     {"Diagonală": "55 inch", "Rezoluție": "4K UHD", "Panou": "QLED", "HDR": "HDR10+ / HLG", "Design": "Cadru de tablou", "Art Mode": "Da", "Smart TV": "Tizen"},
     ["https://i.imgur.com/qZmVkxR.png"]),


    ("Samsung WW90T534DAW Mașină de Spălat", "Samsung", "Electrocasnice mari", 2799.99, 8,
     {"Capacitate": "9 kg", "Turații": "1400 rpm", "Eficiență energetică": "A", "Motor": "Digital Inverter", "Programe": "23", "Abur": "Steam Wash", "Display": "AI Control"},
     ["https://i.imgur.com/ZmVkRxq.png"]),

    ("Samsung RB38C776CS9 Frigider", "Samsung", "Electrocasnice mari", 4299.99, 5,
     {"Tip": "Combină frigorifică", "Capacitate": "387 litri", "Eficiență": "C", "No Frost": "Da", "Compresor": "Digital Inverter", "Display": "Wi-Fi / SmartThings", "Răcire": "All-around Cooling"},
     ["https://i.imgur.com/mVkRxqZ.png"]),

    ("Bosch WGG244A0BY Mașină de Spălat", "Bosch", "Electrocasnice mari", 3199.99, 7,
     {"Capacitate": "9 kg", "Turații": "1400 rpm", "Eficiență energetică": "A", "Motor": "EcoSilence Drive", "Programe": "14", "AntiPată": "Da", "SpeedPerfect": "Da"},
     ["https://i.imgur.com/VkRxqZm.png"]),

    ("Bosch Serie 6 BGC41XSIL Aspirator", "Bosch", "Electrocasnice mari", 1299.99, 12,
     {"Tip": "Fără sac", "Putere": "700W", "Filtru": "HEPA", "Capacitate recipient": "1.7 litri", "Rază acțiune": "11m", "Greutate": "4.4 kg", "Zgomot": "70 dB"},
     ["https://i.imgur.com/kRxqZmV.png"]),

    ("Dyson V15 Detect", "Dyson", "Electrocasnice mari", 3999.99, 6,
     {"Tip": "Aspirator vertical fără fir", "Autonomie": "60 min", "Putere aspirare": "240AW", "Laser": "Detectare particule", "Ecran LCD": "Afișare particule", "Filtrare": "HEPA", "Capacitate": "0.76 litri"},
     ["https://i.imgur.com/xRqZVmk.png"]),

    ("Dyson Supersonic HD15", "Dyson", "Electrocasnice mari", 2199.99, 10,
     {"Tip": "Uscător de păr", "Putere": "1600W", "Motor": "V9 Digital", "Viteze": "3", "Temperaturi": "4", "Protecție termică": "Da", "Atașamente": "5 incluse"},
     ["https://i.imgur.com/RqZVmkx.png"]),

    ("LG F4WR709S2W Mașină de Spălat", "LG", "Electrocasnice mari", 2599.99, 9,
     {"Capacitate": "9 kg", "Turații": "1400 rpm", "Eficiență energetică": "A", "Motor": "Direct Drive AI", "Steam": "Da", "ThinQ": "Wi-Fi", "Programe": "14"},
     ["https://i.imgur.com/qZVmkxR.png"]),

    ("LG GSXV91MCAE Frigider Side by Side", "LG", "Electrocasnice mari", 6499.99, 3,
     {"Tip": "Side by Side", "Capacitate": "635 litri", "Eficiență": "E", "No Frost": "Total", "InstaView": "Door-in-Door", "Compresor": "Linear Inverter", "Dispenser": "Apă + Gheață"},
     ["https://i.imgur.com/ZVmkxRq.png"]),

    ("Philips EP2231/40 Espressor Automat", "Philips", "Electrocasnice mari", 1899.99, 14,
     {"Tip": "Espressor automat", "Presiune": "15 bari", "Râșniță": "Ceramică", "Rezervor apă": "1.8 litri", "Băuturi": "3 (espresso, cafea, cappuccino)", "Spumă lapte": "LatteGo", "Display": "Touch"},
     ["https://i.imgur.com/VmkxRqZ.png"]),

    ("iRobot Roomba j9+", "iRobot", "Electrocasnice mari", 4999.99, 5,
     {"Tip": "Aspirator robot", "Navigare": "PrecisionVision", "Golire automată": "Clean Base", "Autonomie": "120 min", "Mapping": "Da", "App": "iRobot Home", "Obstacole": "Evitare AI"},
     ["https://i.imgur.com/mkxRqZV.png"]),


    ("PlayStation 5 Slim", "Sony", "Gaming", 2799.99, 10,
     {"Stocare": "1TB SSD", "Rezoluție": "4K 120Hz / 8K", "Ray Tracing": "Da", "Audio 3D": "Tempest Engine", "Disc": "Blu-ray Ultra HD", "Controller": "DualSense inclus", "Backward": "PS4 compatibil"},
     ["https://i.imgur.com/kxRZqmV.png"]),

    ("DualSense Wireless Controller", "Sony", "Gaming", 349.99, 25,
     {"Compatibilitate": "PS5 / PC", "Feedback haptic": "Da", "Adaptive Triggers": "Da", "Microfon": "Integrat", "Baterie": "Reîncărcabilă Li-ion", "USB-C": "Da", "Culoare": "Alb"},
     ["https://i.imgur.com/xRZqmVk.png"]),

    ("Xbox Series X", "Microsoft", "Gaming", 2999.99, 8,
     {"Stocare": "1TB SSD", "Rezoluție": "4K 120fps / 8K", "Procesor": "AMD Zen 2 8-core", "GPU": "12 TFLOPS RDNA 2", "RAM": "16GB GDDR6", "Disc": "Blu-ray 4K", "Quick Resume": "Da"},
     ["https://i.imgur.com/RZqmVkx.png"]),

    ("Xbox Wireless Controller", "Microsoft", "Gaming", 299.99, 30,
     {"Compatibilitate": "Xbox / PC / Mobile", "Bluetooth": "5.0", "USB-C": "Da", "Textură grip": "Da", "Share button": "Da", "Baterie": "AA (incluse)", "Culoare": "Carbon Black"},
     ["https://i.imgur.com/ZqmVkxR.png"]),

    ("Nintendo Switch OLED", "Nintendo", "Gaming", 1899.99, 12,
     {"Ecran": "7 inch OLED", "Rezoluție": "1280x720 (portabil) / 1080p (TV)", "Stocare": "64GB", "Autonomie": "4.5 - 9 ore", "Joy-Con": "2 incluse", "Dock": "Inclus", "LAN port": "Da"},
     ["https://i.imgur.com/qmVkxRZ.png"]),

    ("Razer DeathAdder V3", "Razer", "Gaming", 449.99, 20,
     {"Tip": "Mouse gaming", "Senzor": "Focus Pro 30K", "DPI": "30000", "Butoane": "5", "Switch-uri": "Optice Gen-3", "Greutate": "59g", "Cablu": "Speedflex"},
     ["https://i.imgur.com/mVkRxZq.png"]),

    ("Razer BlackWidow V4", "Razer", "Gaming", 899.99, 15,
     {"Tip": "Tastatură mecanică", "Switch-uri": "Razer Green", "Layout": "Full-size", "Iluminare": "Razer Chroma RGB", "Macro keys": "6", "Wrist rest": "Magnetic", "Media keys": "Da"},
     ["https://i.imgur.com/VkRxZqm.png"]),

    ("Logitech G502 X Plus", "Logitech", "Gaming", 699.99, 18,
     {"Tip": "Mouse gaming wireless", "Senzor": "HERO 25K", "DPI": "25600", "Butoane": "13", "Switch-uri": "LIGHTFORCE Hybrid", "Baterie": "130 ore", "LIGHTSPEED": "Da"},
     ["https://i.imgur.com/kRxZqmV.png"]),

    ("Logitech G Pro X TKL", "Logitech", "Gaming", 799.99, 14,
     {"Tip": "Tastatură mecanică", "Switch-uri": "GX Brown Tactile", "Layout": "TKL", "Iluminare": "LIGHTSYNC RGB", "LIGHTSPEED": "Wireless", "Baterie": "50 ore", "USB-C": "Da"},
     ["https://i.imgur.com/xRxZqmV.png"]),

    ("SteelSeries Arctis Nova 7", "SteelSeries", "Gaming", 899.99, 16,
     {"Tip": "Căști gaming wireless", "Drivere": "40mm Nova", "Frecvență": "20 - 22000 Hz", "Microfon": "ClearCast Gen 2 retractabil", "Baterie": "38 ore", "Multi-platform": "PC / PS / Switch / Mobile", "ANC": "Nu"},
     ["https://i.imgur.com/RxZqmVk.png"]),

    ("HyperX Cloud III Wireless", "HyperX", "Gaming", 749.99, 12,
     {"Tip": "Căști gaming wireless", "Drivere": "53mm", "Frecvență": "10 - 21000 Hz", "Microfon": "Detașabil", "Baterie": "120 ore", "DTS": "Headphone:X Spatial Audio", "Greutate": "330g"},
     ["https://i.imgur.com/xZqmRVk.png"]),


    ("Apple AirPods Pro 2 USB-C", "Apple", "Accesorii", 1399.99, 20,
     {"Tip": "Căști wireless in-ear", "ANC": "Activ adaptiv", "Transparency": "Da", "Chip": "H2", "Audio": "Spatial Audio + head tracking", "Baterie": "6h (30h cu carcasa)", "Rezistență": "IP54"},
     ["https://i.imgur.com/ZqmRVkx.png"]),

    ("Apple Watch Series 9 GPS 45mm", "Apple", "Accesorii", 2499.99, 12,
     {"Ecran": "1.9 inch Always-On Retina LTPO OLED", "Chip": "S9 SiP", "Senzori": "SpO2, ECG, Temperatură", "Rezistență apă": "WR50", "NFC": "Apple Pay", "Stocare": "64GB", "Baterie": "18 ore"},
     ["https://i.imgur.com/qmRVkxZ.png"]),

    ("Samsung Galaxy Buds3 Pro", "Samsung", "Accesorii", 1199.99, 18,
     {"Tip": "Căști wireless in-ear", "ANC": "Activ inteligent", "Driver": "2-way (planar + dynamic)", "Codec": "SSC HiFi / AAC", "Baterie": "7h (30h cu carcasa)", "Rezistență": "IP57", "360 Audio": "Da"},
     ["https://i.imgur.com/mRVkxZq.png"]),

    ("Samsung Galaxy Watch 6 44mm", "Samsung", "Accesorii", 1699.99, 14,
     {"Ecran": "1.5 inch Super AMOLED", "Procesor": "Exynos W930", "Senzori": "BIA, SpO2, ECG, Temperatură", "RAM": "2GB", "Stocare": "16GB", "Baterie": "425 mAh", "OS": "Wear OS 4"},
     ["https://i.imgur.com/RVkxZqm.png"]),

    ("Sony WH-1000XM5", "Sony", "Accesorii", 1599.99, 15,
     {"Tip": "Căști wireless over-ear", "ANC": "HD Noise Cancelling", "Driver": "30mm Carbon Fiber", "Codec": "LDAC / AAC / SBC", "Baterie": "30 ore", "Multipoint": "2 dispozitive simultan", "Greutate": "250g"},
     ["https://i.imgur.com/VkxZqmR.png"]),

    ("JBL Flip 6", "JBL", "Accesorii", 599.99, 25,
     {"Tip": "Boxa portabilă", "Putere": "30W", "Driver": "Racetrack + tweeter", "Bluetooth": "5.1", "Baterie": "12 ore", "Rezistență": "IP67", "PartyBoost": "Da"},
     ["https://i.imgur.com/kxZqmRV.png"]),

    ("JBL Charge 5", "JBL", "Accesorii", 799.99, 20,
     {"Tip": "Boxa portabilă", "Putere": "40W", "Driver": "Woofer + tweeter", "Bluetooth": "5.1", "Baterie": "20 ore", "Rezistență": "IP67", "Powerbank": "Da (7500 mAh)", "PartyBoost": "Da"},
     ["https://i.imgur.com/xZqmRkV.png"]),

    ("Anker PowerCore 20000mAh", "Anker", "Accesorii", 199.99, 35,
     {"Tip": "Baterie externă", "Capacitate": "20000 mAh", "Porturi": "2x USB-A + 1x USB-C", "Ieșire max": "22.5W", "Intrare": "USB-C 18W", "Indicator": "LED", "Greutate": "350g"},
     ["https://i.imgur.com/ZqmkRxV.png"]),

    ("Logitech MX Master 3S", "Logitech", "Accesorii", 549.99, 16,
     {"Tip": "Mouse wireless", "Senzor": "Darkfield 8000 DPI", "Conectivitate": "Bluetooth + USB Bolt", "Butoane": "7", "Scroll": "MagSpeed electromagnetic", "Baterie": "70 zile", "Multi-device": "3 dispozitive"},
     ["https://i.imgur.com/qmkRxVZ.png"]),

    ("Marshall Major IV", "Marshall", "Accesorii", 649.99, 18,
     {"Tip": "Căști wireless on-ear", "Driver": "40mm custom", "Bluetooth": "5.0", "Baterie": "80+ ore", "Încărcare": "USB-C + wireless Qi", "Microfon": "Integrat", "Pliabile": "Da"},
     ["https://i.imgur.com/mkRxVZq.png"]),

    ("Belkin BoostCharge 15W", "Belkin", "Accesorii", 249.99, 22,
     {"Tip": "Încărcător wireless", "Putere": "15W (Qi)", "Compatibil": "iPhone / Samsung / Qi", "LED": "Indicator încărcare", "Protecții": "Supratensiune / Temperatură", "Cablu inclus": "USB-C 1.2m", "Design": "Plat anti-alunecare"},
     ["https://i.imgur.com/kRxVZqm.png"]),


    ("Tabletă Apple iPad Pro 13 M4", "Apple", "Tablete", 9499.99, 7,
     {"Ecran": "13 inch Ultra Retina XDR OLED", "Procesor": "Apple M4", "RAM": "8GB", "Stocare": "256GB", "Camera": "12MP + LiDAR", "5G": "Opțional", "Apple Pencil": "Pro compatibil"},
     ["https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/ipad-pro-finish-select-202405-13inch-space-black-wifi?wid=940&hei=1112&fmt=png-alpha"]),

    ("Tabletă Apple iPad Air 13 M2", "Apple", "Tablete", 5499.99, 10,
     {"Ecran": "13 inch Liquid Retina", "Procesor": "Apple M2", "RAM": "8GB", "Stocare": "128GB", "Camera": "12MP", "Touch ID": "Da", "USB-C": "Da"},
     ["https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/ipad-air-finish-select-202405-13inch-starlight-wifi?wid=940&hei=1112&fmt=png-alpha"]),

    ("Tabletă Apple iPad 10.9 inch", "Apple", "Tablete", 2499.99, 15,
     {"Ecran": "10.9 inch Liquid Retina", "Procesor": "A14 Bionic", "RAM": "4GB", "Stocare": "64GB", "Camera": "12MP", "Touch ID": "Buton lateral", "USB-C": "Da"},
     ["https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/ipad-10th-gen-finish-select-202212-blue-wifi?wid=940&hei=1112&fmt=png-alpha"]),

    ("Tabletă Samsung Galaxy Tab S9 Ultra", "Samsung", "Tablete", 7999.99, 5,
     {"Ecran": "14.6 inch Dynamic AMOLED 2X 120Hz", "Procesor": "Snapdragon 8 Gen 2", "RAM": "12GB", "Stocare": "256GB", "Camera": "13MP + 8MP", "S Pen": "Inclus", "Baterie": "11200 mAh"},
     ["https://images.samsung.com/is/image/samsung/p6pim/ro/2308/gallery/ro-galaxy-tab-s9-ultra-wifi-sm-x910nzaaeue-thumb-537229001"]),

    ("Tabletă Samsung Galaxy Tab S9 FE", "Samsung", "Tablete", 2999.99, 12,
     {"Ecran": "10.9 inch TFT LCD 90Hz", "Procesor": "Exynos 1380", "RAM": "6GB", "Stocare": "128GB", "Camera": "8MP", "S Pen": "Inclus", "Baterie": "8000 mAh"},
     ["https://images.samsung.com/is/image/samsung/p6pim/ro/2310/gallery/ro-galaxy-tab-s9-fe-wifi-sm-x510nzgaeue-thumb-538574551"]),

    ("Tabletă Samsung Galaxy Tab A9+", "Samsung", "Tablete", 1499.99, 18,
     {"Ecran": "11 inch TFT LCD 90Hz", "Procesor": "Snapdragon 695", "RAM": "4GB", "Stocare": "64GB", "Camera": "8MP", "Baterie": "7040 mAh", "Boxe": "Quad stereo AKG"},
     ["https://images.samsung.com/is/image/samsung/p6pim/ro/2310/gallery/ro-galaxy-tab-a9-plus-wifi-sm-x210nzaaeue-thumb-538575001"]),

    ("Tabletă Lenovo Tab P12", "Lenovo", "Tablete", 2199.99, 10,
     {"Ecran": "12.7 inch 2K IPS", "Procesor": "MediaTek Dimensity 7050", "RAM": "8GB", "Stocare": "128GB", "Camera": "13MP", "Baterie": "10200 mAh", "Stylus": "Inclus"},
     ["https://p1-ofp.static.pub/fes/cms/2023/11/06/lenovo-tab-p12-hero.png"]),

    ("Tabletă Xiaomi Pad 6", "Xiaomi", "Tablete", 1799.99, 14,
     {"Ecran": "11 inch 2.8K IPS 144Hz", "Procesor": "Snapdragon 870", "RAM": "8GB", "Stocare": "128GB", "Camera": "13MP", "Baterie": "8840 mAh", "Încărcare": "33W"},
     ["https://i02.appmifile.com/174_operator_sg/07/06/2023/xiaomi-pad-6-hero.png"]),

    ("Tabletă Huawei MatePad 11.5", "Huawei", "Tablete", 1899.99, 8,
     {"Ecran": "11.5 inch 2.2K IPS 120Hz", "Procesor": "Snapdragon 7 Gen 1", "RAM": "8GB", "Stocare": "128GB", "Camera": "13MP", "Baterie": "7700 mAh", "HarmonyOS": "4"},
     ["https://consumer.huawei.com/content/dam/huawei-cbg-site/common/mkt/pdp/tablets/matepad-11-5/img/kv/huawei-matepad-11-5-kv.png"]),

    ("Tabletă Microsoft Surface Pro 9", "Microsoft", "Tablete", 7499.99, 4,
     {"Ecran": "13 inch PixelSense Flow 120Hz", "Procesor": "Intel Core i7-1255U", "RAM": "16GB", "Stocare": "256GB SSD", "Baterie": "15.5 ore", "Windows": "11 Pro", "Kickstand": "165°"},
     ["https://img-prod-cms-rt-microsoft-com.akamaized.net/cms/api/am/imageFileData/RE4OXzi?ver=3a58"]),


    ("Cuptor cu Microunde Samsung MS23T5018AW 23L", "Samsung", "Electrocasnice mici", 599.99, 20,
     {"Capacitate": "23 litri", "Putere": "800W", "Interior": "Ceramic Enamel", "Display": "LED", "Culoare": "Alb"},
     ["https://images.samsung.com/is/image/samsung/p6pim/ro/ms23t5018aw-ol/gallery/ro-microwave-oven-solo-ms23t5018aw-ol-thumb-532227893"]),

    ("Cuptor cu Microunde LG MH6336GIB 23L", "LG", "Electrocasnice mici", 799.99, 15,
     {"Capacitate": "23 litri", "Putere": "1000W", "Grill": "Da", "Funcții": "Smart Inverter", "Culoare": "Negru"},
     ["https://www.lg.com/content/dam/channel/wcms/ro/microwave-ovens/ms2336gib/gallery/medium01.jpg"]),

    ("Cuptor cu Microunde Bosch BFL524MS0 20L", "Bosch", "Electrocasnice mici", 1499.99, 8,
     {"Capacitate": "20 litri", "Putere": "800W", "Tip": "Încorporabil", "AutoPilot": "8 programe", "Interior": "Inox"},
     ["https://media3.bosch-home.com/Product_Shots/1200x675/BFL524MS0_STP_def.webp"]),

    ("Aspirator Vertical Fără Fir Xiaomi Vacuum Cleaner G11", "Xiaomi", "Electrocasnice mici", 1299.99, 12,
     {"Putere aspirare": "185AW", "Autonomie": "60 min", "Filtrare": "HEPA", "Greutate": "1.74 kg", "Accesorii": "5 incluse"},
     ["https://i02.appmifile.com/956_operator_sg/01/12/2022/xiaomi-vacuum-g11.png"]),

    ("Aspirator Fără Sac Philips PowerPro Expert FC9745", "Philips", "Electrocasnice mici", 899.99, 14,
     {"Putere": "900W", "Capacitate": "2 litri", "Filtru": "HEPA 13", "Zgomot": "76 dB", "Tehnologie": "PowerCyclone 8"},
     ["https://www.p4c.philips.com/CGI-BIN/CPIMG.EXE?reqtype=doorway&doctype=FIRST&sectionid=11&rendition=300"]),

    ("Blender Philips HR3655/00 ProBlend 6 3D", "Philips", "Electrocasnice mici", 499.99, 18,
     {"Putere": "1400W", "Capacitate": "2 litri", "Lame": "ProBlend 6 3D", "Viteze": "3 + Pulse", "Material bol": "Tritan"},
     ["https://www.p4c.philips.com/CGI-BIN/CPIMG.EXE?reqtype=doorway&doctype=FIRST&sectionid=21&rendition=300"]),

    ("Prăjitor de Pâine Russell Hobbs Colours Plus 23330-56", "Russell Hobbs", "Electrocasnice mici", 199.99, 22,
     {"Putere": "1670W", "Fante": "2 extra largi", "Nivele rumenire": "6", "Funcții": "Dezghețare, Reîncălzire", "Culoare": "Roșu"},
     ["https://russellhobbs.com/media/catalog/product/23330-56_main.jpg"]),

    ("Fier de Călcat cu Abur Philips Azur 8000 DST8041", "Philips", "Electrocasnice mici", 449.99, 16,
     {"Putere": "3000W", "Jet de abur": "250g/min", "Talpă": "SteamGlide Elite", "Rezervor": "350 ml", "Auto-oprit": "Da"},
     ["https://www.p4c.philips.com/CGI-BIN/CPIMG.EXE?reqtype=doorway&doctype=FIRST&sectionid=31&rendition=300"]),

    ("Friteuză cu Aer Cald Philips Airfryer XXL HD9285", "Philips", "Electrocasnice mici", 999.99, 10,
     {"Capacitate": "7.3 litri", "Putere": "2225W", "Tehnologie": "Rapid Air", "Display": "Digital touch", "Rețete": "App NutriU"},
     ["https://www.p4c.philips.com/CGI-BIN/CPIMG.EXE?reqtype=doorway&doctype=FIRST&sectionid=41&rendition=300"]),

    ("Storcător de Fructe Philips Viva Collection HR1856", "Philips", "Electrocasnice mici", 449.99, 11,
     {"Putere": "800W", "Gură alimentare": "75mm", "Capacitate suc": "0.8 litri", "Viteze": "2", "Material": "Inox"},
     ["https://www.p4c.philips.com/CGI-BIN/CPIMG.EXE?reqtype=doorway&doctype=FIRST&sectionid=51&rendition=300"]),


    ("Aragaz Beko FSET52324DXDS 4 Arzătoare", "Beko", "Electrocasnice mari", 1999.99, 7,
     {"Tip": "Aragaz mixt", "Arzătoare": "4 pe gaz", "Cuptor": "Electric 72L", "Clasă energetică": "A", "Dimensiuni": "50 x 60 cm"},
     ["https://www.beko.com/content/dam/beko-aem/romania/cooking/freestanding-cookers/fset52324dxds_01.png"]),

    ("Aragaz Arctic ARSGM12612GBK 4 Arzătoare", "Arctic", "Electrocasnice mari", 1499.99, 10,
     {"Tip": "Aragaz pe gaz", "Arzătoare": "4 pe gaz", "Cuptor": "Pe gaz 48L", "Aprindere": "Electrică", "Dimensiuni": "50 x 60 cm"},
     ["https://www.arctic.ro/media/catalog/product/arsgm12612gbk_01.png"]),

    ("Frigider cu Două Uși Samsung RT38CG6624S9 382L", "Samsung", "Electrocasnice mari", 3299.99, 6,
     {"Tip": "Cu două uși", "Capacitate": "382 litri", "No Frost": "Total", "Compresor": "Digital Inverter", "Răcire": "All-around Cooling"},
     ["https://images.samsung.com/is/image/samsung/p6pim/ro/rt38cg6624s9-ef/gallery/ro-top-mount-freezer-rt38cg6624s9-ef-538193001"]),

    ("Mașină de Spălat Vase Bosch SMS4HVW33E 13 Seturi", "Bosch", "Electrocasnice mari", 2499.99, 8,
     {"Capacitate": "13 seturi", "Programe": "6", "Motor": "EcoSilence Drive", "AquaStop": "Da", "Zgomot": "44 dB"},
     ["https://media3.bosch-home.com/Product_Shots/1200x675/SMS4HVW33E_STP_def.webp"]),
]


def populate():
    try:
        conn = psycopg2.connect(**DB_CONFIG)
        cur = conn.cursor()

        category_ids = {}
        for cat_name, cat_desc in CATEGORIES:
            cur.execute("SELECT id FROM categories WHERE name = %s", (cat_name,))
            row = cur.fetchone()
            if row:
                category_ids[cat_name] = row[0]
            else:
                cur.execute(
                    "INSERT INTO categories (name, description) VALUES (%s, %s) RETURNING id",
                    (cat_name, cat_desc)
                )
                cat_id = cur.fetchone()[0]
                category_ids[cat_name] = cat_id
                print(f"Categorie creata: {cat_name} (ID: {cat_id})")

        conn.commit()

        added = 0
        skipped = 0
        for name, brand, category, price, stock, specs, images in PRODUCTS:
            cur.execute("SELECT id FROM products WHERE name = %s", (name,))
            existing = cur.fetchone()

            if existing:
                skipped += 1
                continue

            cat_id = category_ids[category]
            specs_json = json.dumps(specs, ensure_ascii=False)

            cur.execute("""
                INSERT INTO products (name, brand, category_id, price, stock_quantity, specs) 
                VALUES (%s, %s, %s, %s, %s, %s::jsonb)
                RETURNING id
            """, (name, brand, cat_id, price, stock, specs_json))

            product_id = cur.fetchone()[0]

            for img_url in images:
                cur.execute("""
                    INSERT INTO product_images (product_id, image_url)
                    VALUES (%s, %s)
                """, (product_id, img_url))

            import random
            views = random.randint(50, 500)
            cur.execute("""
                INSERT INTO product_analytics (product_id, view_count)
                VALUES (%s, %s)
            """, (product_id, views))

            added += 1
            print(f"Adaugat: {name} ({brand}) - {price} lei")

        conn.commit()

        print(f"\nAdaugate: {added} produse, skip: {skipped}")

        cur.close()
        conn.close()
    except Exception as e:
        print(f"Eroare: {e}")


if __name__ == "__main__":
    populate()
