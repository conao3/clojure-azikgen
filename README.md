# azikgen

A Clojure tool for generating Japanese input method mapping tables from declarative YAML configurations.

## Overview

azikgen generates romaji-to-kana conversion tables from rule-based YAML definitions. It supports multiple rule types that can be combined to create complex input mappings such as AZIK, an optimized Japanese keyboard input method.

## Requirements

- Java 11 or later
- Clojure CLI (clj)

## Installation

Clone the repository:

```sh
git clone https://github.com/conao3/clojure-azikgen.git
cd clojure-azikgen
```

## Usage

Generate a mapping table from a YAML configuration file:

```sh
clj -M -m azikgen.core sample/azik.yml
```

The output is a JSON object mapping input sequences to kana characters:

```json
{
  "a": "あ",
  "ka": "か",
  "kp": "こう",
  "sya": "しゃ"
}
```

## Rule Types

azikgen supports four rule types that can be combined in a single configuration file:

### matrix-rule

Defines a base vowel matrix combined with consonant prefixes:

```yaml
- type: matrix-rule
  base: [a, i, u, e, o]
  matrix:
    k: [か, き, く, け, こ]
    s: [さ, し, す, せ, そ]
```

### postfix-rule

Applies postfix characters to consonant combinations:

```yaml
- type: postfix-rule
  postfix:
    a: ゃ
    u: ゅ
    o: ょ
  matrix:
    ky: き
    sy: し
```

### raw-rule

Defines explicit key-value mappings:

```yaml
- type: raw-rule
  matrix:
    fu: ふ
    nn: ん
```

### dependent-macro-rule

Creates mappings that depend on previously defined rules:

```yaml
- type: dependent-macro-rule
  macro:
    n: ann
    j: unn
  pre: [k, s, t, n, h]
```

## Sample Configurations

The `sample/` directory contains example configurations:

- `azik.yml` - Full AZIK input method mappings
- `roma.yml` - Standard romaji input mappings

## Running Tests

```sh
clj -M:test
```

## License

This project is open source.
