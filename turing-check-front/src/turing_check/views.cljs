(ns turing-check.views
  (:require
    [re-frame.core :as rf]
    [reagent.core :refer [atom]]
    [turing-check.events]
    [turing-check.subs]
    [turing-check.config :as config]))

(defn button [text action prop] [:button (merge prop {:on-click action}) text])

(defn a-input [type value prop]
  [:input (merge prop {:type type :value @value :on-change #(reset! value (-> % .-target .-value))})])

(def page-items
  {:description ["Описание" {:placeholder "Зачем?"}]
   :program ["Программа (пустышка: ∧, начальное: q0)" {:placeholder "q, a -> p, b, -1" :rows 20}]
   :tests ["Тесты" {:placeholder "123 -> 321" :rows 20}]})

(defn page-in [field name prop]
  (let [value (rf/subscribe [:page field])]
    [:div
     [:label name]
     [:textarea (merge prop
                       {:value @value
                        :on-change #(rf/dispatch [:update-page field (-> % .-target .-value)])})]]))

(defn start []
  (let [name (atom "")]
    [:div.search
     [a-input "text" name {:placeholder "Название"}]
     [button "Открыть" #(rf/dispatch [:open @name])]
     [button "Создать" #(rf/dispatch [:create @name])]]))

(defn test-save []
  (let [is-example? (rf/subscribe [:is-example?])]
    [:div {:align :right}
     [button "Запустить" #(rf/dispatch [:test])]
     [button "Сохранить" #(rf/dispatch [:save]) {:disabled @is-example?}]]))

(defn edit []
  (let [name (rf/subscribe [:page :name])]
    [:div.page
     [:h3 @name]
     [test-save]
     (for [[k [name prop]] page-items] ^{:key k} [page-in k name prop])
     [test-save]]))

(defn decision->text [decision]
  (case decision
    "Accepted" "Принято"
    "Failed" "Ошибка"
    "Wrong" "Не тест"
    "NotStandard" "Нестандартная"
    "InfiniteLoop" "Зацикливание"
    "Неизвестно"))

(defn result-item [{:keys [test decision]}]
  [:li.row
   [:div.column.left test]
   [:div.column.middle (decision->text decision)]
   [:div.column.right [button "Анализировать" nil {:disabled true}]]])

(defn result []
  [:div.result
   [:ol
    (for [{:keys [test] :as item} @(rf/subscribe [:result])] ^{:key test}
      [result-item item])]])

(defn menu []
  (let [has-last? (rf/subscribe [:has-last?])]
    [:div.menu
     [button "Поиск" #(rf/dispatch [:to-state :start])]
     [button "Редактировать" #(rf/dispatch [:open-last]) {:disabled (not @has-last?)}]
     [button "Пример" #(rf/dispatch [:open config/example-name])]]))

(defn alert []
  (let [alert (rf/subscribe [:alert])]
    (if (some? @alert)
      (let [[type text] @alert]
        [:div {:class ["box" type] :on-click #(rf/dispatch [:no-alert])} text]))))

(defn main-panel []
  (let [state (rf/subscribe [:state])]
    [:div
     [:h1 [button "Turing Check" #(rf/dispatch [:to-state :start])]]
     [menu]
     [alert]
     [(case @state
       :start start
       :edit edit
       :result result
       :analyze nil)]
     [:footer>a {:href config/source :style {:color :black}} "Source code"]]))
