(ns turing-check.core
  (:require
    [reagent.dom :as rdom]
    [re-frame.core :as rf]
    [day8.re-frame.http-fx]
    [turing-check.events]
    [turing-check.views :as views]
    [turing-check.config :as config]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")
    (println config/host)))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
