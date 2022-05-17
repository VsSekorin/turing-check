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

(defn page-in [tag field name prop]
  (let [value (rf/subscribe [:page field])]
    [:div
     [:label name]
     [tag (merge prop
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
     [page-in :textarea :description "Описание" {:placeholder "Зачем?"}]
     [:div
      [page-in :input :empty "Пустышка"]
      [page-in :input :initState "Начальное состояние"]]
     [page-in :textarea :program "Программа" {:placeholder "q, a -> p, b, -1" :rows 20}]
     [page-in :textarea :tests "Тесты" {:placeholder "123 -> 321" :rows 20}]
     [test-save]]))

(defn decision->text [decision]
  (case decision
    "NotTest" "Не тест"
    "Correct" "Принято"
    "Incorrect" "Ошибка"
    "NonStandard" "Нестандартная"
    "InfiniteLoop" "Зацикливание"
    "Неизвестно"))

(defn result-item [{:keys [test decision]}]
  [:li {:class [:row (if (= decision "Correct") :good :error)]}
   [:div.column.left test]
   [:div.column.middle (decision->text decision)]
   [:div.column.right [button "Подробнее" nil {:disabled true}]]])

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
        [:div {:class [:box type] :on-click #(rf/dispatch [:no-alert])} text]))))

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
