# cljs-reframe-template

A starter https://github.com/Day8/re-frame[re-frame] application.

frontend routing with https://github.com/metosin/reitit[reitit]

dev tooling with shadow-cljs

styles per inline injection with https://github.com/noprompt/garden[CSS Garden]

styles via TailwindCSS https://tailwindcss.com/

## Development Mode


```bash
npx shadow-cljs watch app
```


## Compile application

```bash
npx shadow-cljs compile app
```

```bash
npx shadow-cljs release app
```

## CSS via Tailwind

- CSS artifacts are compiled
    - via Tailwind's new JIT Engine
- using https://github.com/teknql/shadow-cljs-tailwind-jit
- so one can use
    - build hooks
    - inside Shadow-Cljs projects

## manually purge CSS

```bash
npm run-script tw
```

## Run application:

Wait a bit, then browse to http://localhost:8021.

## Using babashka

https://github.com/babashka/babashka[babashka]

- use of the new babashka tasks feature
- config via bb.edn

```bash
bb tasks
```

```bash
bb watch
bb compile
bb release
bb purgecss
bb buildreport
```

# Credits
Service Bell Sound Effect
Sound Effect by [freesound_community](https://pixabay.com/users/freesound_community-46691455/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=106698) from [Pixabay](https://pixabay.com/sound-effects//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=106698)
