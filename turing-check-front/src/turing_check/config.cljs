(ns turing-check.config)

(def debug?
  ^boolean goog.DEBUG)

(def host
  (if debug?
    "http://localhost:8080/"
    "https://turing-check.herokuapp.com/"))

(def source "https://github.com/VsSekorin/turing-check")

(def example-name "example")
