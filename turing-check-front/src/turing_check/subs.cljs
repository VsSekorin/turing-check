(ns turing-check.subs
  (:require
    [re-frame.core :as rf]
    [turing-check.config :as config]))

(rf/reg-sub
  :state
  (fn [db] (:state db)))

(rf/reg-sub
  :alert
  (fn [db] (:alert db)))

(rf/reg-sub
  :result
  (fn [db] (:result db)))

(rf/reg-sub
  :page
  (fn [db [_ field]]
    (get-in db [:page field] "")))

(rf/reg-sub
  :is-example?
  (fn [db _] (= (:name (:page db)) config/example-name)))

(rf/reg-sub
  :has-last?
  (fn [db _] (some? (:last db))))
