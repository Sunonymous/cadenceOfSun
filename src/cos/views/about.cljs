(ns cos.views.about
    (:require
     [reagent.core :as reagent]
    ))

(def text-versions
  [
   "Call me Sunny. I’m here to help people feel capable, curious, and clear-headed—whether they’re learning something new, getting organized, or trying to make a change. I use tools like software, structure, and storytelling to make that journey easier, calmer, and more personal."
   "My name is Sunny. I believe in helping people find ease in the work of being human. Whether it’s organizing thoughts, navigating change, or learning something new, I design tools and experiences that support clarity, confidence, and growth."
   "Hey, I'm Sunny. My mission is to enrich people’s lives by reducing friction in the everyday—supporting learning, organization, and growth through clear systems and thoughtful communication. Technology is just one way I help people feel more capable and less overwhelmed."
   "I'm Sunny ☀️—a maker, parent, and lifelong learner with a deep interest in human systems, clear design, and personal mastery. I create thoughtful tools for practice and self-exploration. My background bridges technical implementation with people-focused design, and I care most about work that empowers, clarifies, or uplifts."
  ])

(defn main []
  (let [text-index* (reagent.core/atom 0)
        next-text! #(swap! text-index* (fn [i] (mod (inc i) (count text-versions))))]
    (fn []
      [:div.p-4
       {:class ["width-full" :p-2 "rounded-[5px]"]}
       [:h2.text-4xl.thick "· about ·"]
       [:p.my-4 (nth text-versions @text-index*)]
       [:button.p-2.rounded.border-2.bg-white
        {:on-click next-text!}
        "Next"]])))