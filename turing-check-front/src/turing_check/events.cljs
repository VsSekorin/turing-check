(ns turing-check.events
  (:require
   [ajax.core :as ajax]
   [re-frame.core :as rf]
   [turing-check.db :as db]
   [turing-check.config :as config]))

(defn uri [s] (str config/host s))

(defn set-last [db name]
 (if (not= name config/example-name)
  (assoc db :last name)
  db))

(rf/reg-event-db
 :initialize-db
 (fn [_ _] db/default-db))

(rf/reg-event-db
 :to-state
 (fn [db [_ state]]
  (-> db
      (assoc :alert nil)
      (assoc :state state))))

(rf/reg-event-fx
 :create
 (fn [{:keys [db]} [_ name]]
  {:http-xhrio {:uri             (uri "page/create")
                :method          :post
                :params          {:name name}
                :timeout         config/http-timeout
                :format          (ajax/url-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success [:open-on-success]
                :on-failure [:create-failure]}
   :db (assoc db :alert [:info "Создаю..."])}))

(rf/reg-event-db
 :create-failure
 (fn [db _]
  (assoc db :alert [:error "Невозможно создать с таким именем."])))

(rf/reg-event-db
 :open-last
 (fn [db _]
  (rf/dispatch [:open (:last db)])))

(rf/reg-event-fx
 :open
 (fn [{:keys [db]} [_ name]]
  {:http-xhrio {:uri             (uri "page/open")
                :method          :get
                :params          {:name name}
                :timeout         config/http-timeout
                :format          (ajax/url-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success [:open-on-success]
                :on-failure [:open-failure]}
   :db (assoc db :alert [:info "Ищу..."])}))

(rf/reg-event-db
 :open-on-success
 (fn [db [_ page]]
  (-> db
      (assoc :alert nil)
      (assoc :page page)
      (set-last (:name page))
      (assoc :state :edit))))

(rf/reg-event-db
 :open-failure
 (fn [db _]
  (assoc db :alert [:error "Не найдено с таким именем."])))

(rf/reg-event-db
 :no-alert
 (fn [db _]
  (assoc db :alert nil)))

(rf/reg-event-db
 :update-page
 (fn [db [_ field val]]
  (assoc-in db [:page field] val)))

(rf/reg-event-fx
 :save
 (fn [{:keys [db]} _]
  {:http-xhrio {:uri             (uri "page/save")
                :method          :put
                :params          (:page db)
                :timeout         config/http-timeout
                :format          (ajax/json-request-format)
                :response-format (ajax/raw-response-format)
                :on-success [:save-success]
                :on-failure [:save-failure]}
   :db (assoc db :alert [:info "Сохраняю..."])}))

(rf/reg-event-db
 :save-success
 (fn [db _]
  (assoc db :alert [:good "Сохранено."])))

(rf/reg-event-db
 :save-failure
 (fn [db _]
  (assoc db :alert [:error "Не удалось сохранить."])))

(rf/reg-event-fx
 :test
 (fn [{:keys [db]} _]
  {:http-xhrio {:uri             (uri "page/test")
                :method          :post
                :params          (:page db)
                :timeout         config/http-timeout
                :format          (ajax/json-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success [:test-success]
                :on-failure [:test-failure]}
   :db (assoc db :alert [:info "Проверяю..."])}))

(rf/reg-event-db
 :test-success
 (fn [db [_ {:keys [result]}]]
  (-> db
      (assoc :alert nil)
      (assoc :result result)
      (assoc :state :result))))

(rf/reg-event-db
 :test-failure
 (fn [db _]
  (assoc db :alert [:error "Невозможно запустить. Проверьте программу."])))
