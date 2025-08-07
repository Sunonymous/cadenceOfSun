(ns pantry.db)

;;
; foods have a shape like
; {:name "Wild Honey"
;  :category "Treat"  ; categories are only useful when shared by multiple foods
;  :img "https://notgoingtofindapic.com/should_be_pic_of_honey.jpg"
; }
; it is IMPORTANT to keep the name the same as the key of the :foods map
;  due to the shoddy nature of the original design. cheers.

(def example-foods
  [{:name     "Apple"
    :category "fruit"
    :img      "https://upload.wikimedia.org/wikipedia/commons/a/a6/Pink_lady_and_cross_section.jpg"}
   {:name     "Grapefruit"
    :category "mislabeled fruit (fix it!)"
    :img      "https://upload.wikimedia.org/wikipedia/commons/4/4c/Bananas.jpg"}
   {:name     "Rotten Apple"
    :category "trash fruit (remove it 🗑️!)"
    :img      "https://upload.wikimedia.org/wikipedia/commons/c/c6/Madige-Apfel-Frucht.jpg"}])

(def default-db
  {:target-food nil ; basically the food (name) that is currently selected/edited/viewed
   :foods       {}
   })