(ns clojure-usage.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn file->symbols [f]
  (->>
   (str "(" (slurp f) ")")
   read-string
   flatten
   (filter (fn [s] (or (symbol? s) (keyword? s))))
   (map (fn [s] (if (symbol? s) (symbol (name s)) s)))
   ))

(defn top-usages [src-dir]
  (->> src-dir
    io/file
    file-seq
    (filter (fn [f] (and (.isFile f) (re-matches #".*\.clj[cs]*" (str f)))))
    (map file->symbols)
    flatten
    frequencies
    (sort-by second)
    reverse
    (take 50)))

(comment
  (->>
    [ "aoc2018" "aoc2019" "aoc2020" "aoc2021" "aoc2022"]
    (map (partial str "/home/john/workspace/"))
    (map (juxt identity top-usages)))

  ;
  )


