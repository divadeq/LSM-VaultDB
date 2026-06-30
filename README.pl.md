# Własna implementacja bazy danych NoSQL typu key-value w Javie
_Przeczytaj to w: [English](README.md) / [Polski](README.pl.md)_

Baza danych NoSQL typu key-value zbudowana od podstaw w Javie. System został zaprojektowany w oparciu o architekturę log-structured storage (inspirowaną Bitcask), łącząc indeks w pamięci z trwałym, binarnym, append-only przechowywaniem danych na dysku.

---

## Przegląd architektury systemu

Aby osiągnąć maksymalną przepustowość i zminimalizować wąskie gardła operacji wejścia/wyjścia (disk I/O), silnik dzieli zarządzanie danymi na dwie warstwy:
1. **Warstwa trwała (Dysk / Persistent Layer):** Binarna struktura plików typu append-only, w której każda operacja zapisu jest sekwencyjnie dopisywana na sam koniec.
2. **Warstwa ulotna (RAM / Volatile Layer):** Aktywna pamięć podręczna (cache), która przechowuje najczęściej używane klucze, całkowicie eliminując powolne odczyty z dysku.

---

## Warstwa zarządzania pamięcią (`LruEvictionPolicy`)

Głównym komponentem kontrolującym aktywną pamięć podręczną w RAM-ie jest klasa `LruEvictionPolicy`. Zapobiega ona przepełnieniu pamięci aplikacji poprzez usuwanie najdawniej używanych kluczy, gdy baza osiągnie limit swojej pojemności.

### Struktura danych
Aby zapewnić, że wszystkie operacje zarządzania pamięcią podręczną będą wykonywane w czasie stałym **O(1)**, komponent ten wykorzystuje hybrydową strukturę danych:
* **Własna lista dwukierunkowa (Doubly Linked List):** Utrzymuje dokładną kolejność użycia danych. Najświeższe elementy są przenoszone na początek listy (`head`), podczas gdy najstarsze naturalnie dryfują w stronę końca listy (`tail`).
* **Java `HashMap`:** Mapuje klucze bezpośrednio na odpowiadające im referencje węzłów (`Node`) wewnątrz listy, co pozwala uniknąć kosztownego i wolnego przeszukiwania sekwencyjnego.