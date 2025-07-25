(ns kitchen.data)

;; In data, be sure that :name matches the key, otherwise problems happen
;; To use different names for display, use :nickname

(def foods {"Apples"                      {:name     "Apples"
                                           :img      "https://www.applepietrail.ca/wp-content/uploads/elementor/thumbs/gala-ouju5lhnwyyi470y8nbwd4x3v02o63phzfu0hizxfk.jpg"
                                           :category "fruit"}
            "Arepa"                       {:name     "Arepa"
                                           :img      "https://familiakitchen.com/wp-content/uploads/2021/09/arepas-3-e1631375575861-700x659.jpg.webp"
                                           :category "meal"}
            "Blueberries"                 {:name     "Blueberries"
                                           :img      "https://www.dole.com/sites/default/files/styles/1536w1152h-webp-80/public/media/2025-01/blueberries.png.webp?itok=TB7kQfoZ-ZTlJRjuu-RoiUHP2J"
                                           :category "fruit"}
            "Bread"                       {:name     "Bread"
                                           :img      "https://www.seriouseats.com/thmb/yvullEfUT0JuzHm9mWv7Ec7aYW8=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/__opt__aboutcom__coeus__resources__content_migration__serious_eats__seriouseats.com__2018__12__20181220-wheat-bread-loaf-vicky-wasik-30-264ac9dff1b94f2bafb1647b8601fd4a.jpg"
                                           :category "side"}
            "Bread Roll"                  {:name     "Bread Roll"
                                           :img      "https://www.melskitchencafe.com/wp-content/uploads/2014/03/whole-wheat-rolls7-600x399.jpg"
                                           :category "side"}
            ;; "Cachapa"                     {:name "Cachapa", :img "", :category "meal"},
            "Cheerios"                    {:name     "Cheerios"
                                           :img      "https://cdn.cupcakeproject.com/wp-content/uploads/2012/02/Frosted+Cheerios.jpg"
                                           :category "meal"}
            "Cheese, Mozarella, Sliced"   {:name     "Cheese, Mozarella, Sliced"
                                           :nickname "Mozarella Cheese"
                                           :img      "https://gfsstore.com/wp-content/uploads/2025/04/726567-RAW-6-1743865073.jpg"
                                           :category "meal"}
            "Cheese, Shredded"            {:name     "Cheese, Shredded"
                                           :nickname "Sprinkle Cheese"
                                           :img      "https://i5.walmartimages.com/seo/Great-Value-Finely-Shredded-Fiesta-Blend-Cheese-16-oz_0cc11725-aea5-4ce6-a0e5-f82d5688d9e9.dc88776f94f883476f847c689b5673eb.jpeg"
                                           :category "side"}
            "Cherries"                    {:name     "Cherries"
                                           :img      "https://cdn.britannica.com/60/174560-050-5A33606F/cherries-Cluster.jpg"
                                           :category "fruit"}
            "Chocolate Sandwich"          {:name     "Chocolate Sandwich"
                                           :img      "https://annacostafood.wordpress.com/wp-content/uploads/2012/06/mg_8241.jpg"
                                           :category "meal"}
            "Cream of Wheat"              {:name     "Cream of Wheat"
                                           :img      "https://theabsolutefoodie.com/wp-content/uploads/2023/03/cream-of-wheat-recipes.jpg"
                                           :category "breakfast"}
            "Dinosaur Egg Oatmeal"        {:name     "Dinosaur Egg Oatmeal"
                                           :img      "https://poorcouplesfoodguide.com/wp-content/uploads/2016/04/103_1795.jpg?w=508"
                                           :category "breakfast"}
            "Dinosaur Nuggets"            {:name     "Dinosaur Nuggets"
                                           :img      "https://dam.catalog.1worldsync.com/im/dwn/GCP-5102267363491840?v=1&width=1000&height=1000&quality=50&upscale=true&bgcolor=FFFFFF"
                                           :category "meal"}
            "Egg, Pizza"                  {:name     "Egg, Pizza"
                                           :nickname "Pizza Egg"
                                           :img      "https://www.babaganosh.org/wp-content/uploads/2021/05/egg-tortilla-rollups-9-735x735.jpg.webp"
                                           :category "meal"}
            "Egg, Hard-boiled"            {:name     "Egg, Hard-boiled"
                                           :nickname "Hard-boiled Egg"
                                           :img      "https://68.media.tumblr.com/tumblr_m9krxf96GN1qdei8m.jpg"
                                           :category "side"}
            "Fiber Bar"                   {:name     "Fiber Bar"
                                           :img      "https://cdnsc1.melaleuca.com/cdn-cgi/image/width=500height=500,quality=85/global/products/2262h-01-enus.png"
                                           :category "side"}
            "French Fries"                {:name     "French Fries"
                                           :img      "https://munchygoddess.com/wp-content/uploads/2022/02/Air-Fryer-Frozen-Crinkle-Cut-Fries-Recipe-Munchy-Goddess-Hero-1.jpg"
                                           :category "side"}
            "Grilled Cheese"              {:name     "Grilled Cheese"
                                           :img      "https://www.allrecipes.com/thmb/CZ93z2oGv0n9ZsLp5yE2Lgb0Tyk=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/AR-238891-Grilled-Cheese-Sandwich-beauty-4x3-362f705972e64a948b7ec547f7b2a831.jpg"
                                           :category "meal"}
            "Hashbrowns"                  {:name     "Hashbrowns"
                                           :img      "https://brooklynfarmgirl.com/wp-content/uploads/2014/01/Crispy-Hash-Browns_3.jpg"
                                           :category "side"}
            "Huckleberries"               {:name     "Huckleberries"
                                           :img      "https://npr.brightspotcdn.com/dims4/default/2307315/2147483647/strip/true/crop/1920x1370+0+0/resize/1760x1256!/format/webp/quality/90/?url=http%3A%2F%2Fnpr-brightspot.s3.amazonaws.com%2Flegacy%2Fsites%2Fkufm%2Ffiles%2F202008%2FHuckleberries_Skeeze-Pixabay_0.jpg"
                                           :category "fruit"}
            "Ice Cream"                   {:name     "Ice Cream"
                                           :img      "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F43%2F2022%2F11%2F09%2F140877-easy-eggless-strawberry-ice-cream-ddmfs-3x4-1.jpg&q=60&c=sc&poi=auto&orient=true&h=512"
                                           :category "treat"}
            "Kashi Cereal"                {:name     "Kashi Cereal"
                                           :img      "https://m.media-amazon.com/images/I/71CilhFM-LL._SL1500_.jpg"
                                           :category "breakfast"}
            "Laughing Cow Cheese"         {:name     "Laughing Cow Cheese"
                                           :img      "https://www.gfifoods.com/media/catalog/product/cache/f363c0ec9d697ce56276a2431314dc64/5/_/5.4oz20tlc20orig20image_20221108-095436_hxuxem4wpgez1y1n.jpg"
                                           :category "side"}
            "Oreos"                       {:name     "Oreos"
                                           :img      "https://upload.wikimedia.org/wikipedia/commons/1/1b/Oreo-Two-Cookies.png"
                                           :category "treat"}
            "Pancakes"                    {:name     "Pancakes"
                                           :img      "https://myplate-prod.azureedge.us/sites/default/files/styles/recipe_525_x_350_/public/2020-10/Pancakes_527x323.jpg?itok=nsUpQeQi"
                                           :category "breakfast"}
            "Pasta"                       {:name     "Pasta"
                                           :img      "https://www.girlgonegourmet.com/wp-content/uploads/2023/06/Creamy-Garlic-Pasta-4.jpg"
                                           :category "meal"}
            "Peanut Butter Sandwich"      {:name     "Peanut Butter Sandwich"
                                           :img      "https://images.coplusk.net/project_images/4243/image/fluffernutter_small.jpg"
                                           :category "meal"}
            "Peas"                        {:name     "Peas"
                                           :img      "https://www.alphafoodie.com/wp-content/uploads/2023/03/Sauteed-Peas-Main-1.jpeg"
                                           :category "side"}
            "Protein Bar"                 {:name     "Protein Bar"
                                           :img      "https://target.scene7.com/is/image/Target/GUEST_f0afff1d-c960-43e2-a432-d617297b7ff5?wid=1000&hei=1000&qlt=80&fmt=webp"
                                           :category "side"}
            "Raspberries"                 {:name     "Raspberries"
                                           :img      "https://images.cdn.retail.brookshires.com/detail/00033383210001_C1C1.jpeg"
                                           :category "fruit"}
            "Ritz Crackers"               {:name     "Ritz Crackers"
                                           :img      "https://images.ctfassets.net/npgfyzd0fxha/3IsnHkmjQwAFMWw51SIC1B/b8791d0af93b8b75fef1131b2ad37ae6/SNACKWORKS_RITZ.Left.EDIT.png"
                                           :category "side"}
            "Strawberries"                {:name     "Strawberries"
                                           :img      "https://www.dole.com/sites/default/files/styles/1536w1152h-webp-80/public/media/2025-01/strawberries.png.webp?itok=8xtcMvlb-_2t6ysPW-kaq2DL-s}"
                                           :category "fruit"}
            "Sunchips"                    {:name     "Sunchips"
                                           :img      "https://m.media-amazon.com/images/I/81FVPKX1wTL._SL1500_.jpg"
                                           :category "side"}
            "Sunflower Seeds"             {:name     "Sunflower Seeds"
                                           :img      "https://shopsunridgefarms.com/wp-content/uploads/2021/10/002076.jpg"
                                           :category "side"}
            "Three Wishes Cereal"         {:name     "Three Wishes Cereal"
                                           :img      "https://www.mypursestrings.com/wp-content/uploads/2023/08/Three-Wishes-Fruity-cereal-728x410.jpg.webp"
                                           :category "breakfast"}
            "Toast"                       {:name     "Toast"
                                           :img      "https://www.seriouseats.com/thmb/SU6b7MPNLzzmwuY3BO6YLx17LbI=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/__opt__aboutcom__coeus__resources__content_migration__serious_eats__seriouseats.com__2018__12__20181220-wheat-bread-loaf-vicky-wasik-30-264ac9dff1b94f2bafb1647b8601fd4a.jpg"
                                           :category "side"}
            "Tortilla Chips"              {:name     "Tortilla Chips"
                                           :img      "https://www.onelovelylife.com/wp-content/uploads/2022/03/Baked-Tortilla-Chips15-2.jpg"
                                           :category "side"}
            "Waffles"                     {:name     "Waffles"
                                           :img      "https://cdn.apartmenttherapy.info/image/upload/f_auto,q_auto:eco,c_fit,w_730,h_974/k%2FEdit%2F2022-11-Kodiak-Waffles-Review%2Fwaffles_on_plate"
                                           :category "breakfast"}
            "Whipped Cream"               {:name     "Whipped Cream"
                                           :img      "https://natashaskitchen.com/wp-content/uploads/2025/01/Homemade-Whipped-Cream-Recipe-4.jpg"
                                           :category "treat"}
            "Yogurt"                      {:name     "Yogurt"
                                           :img      "https://images.getrecipekit.com/20240109191538-homemade-yogurt.jpg?width=650&quality=90&"
                                           :category "side"}})