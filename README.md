# schema-gen

[![Build Status](https://travis-ci.org/zeeshanlakhani/schema-gen.svg)](https://travis-ci.org/zeeshanlakhani/schema-gen) [![Clojars Project](http://clojars.org/schema-gen/latest-version.svg)](http://clojars.org/schema-gen)

Turn *Prismatic schemas* into generated data.

Always keep-up w/ the [Prismatic](https://github.com/Prismatic/schema) [thread/issue](https://github.com/Prismatic/schema/issues/103) on data-generation from schemas.

All good things here attributed to [@reiddraper](https://github.com/reiddraper), [@davegolland](https://github.com/davegolland), [@w01fe](https://github.com/w01fe), [@MichaelBlume](https://github.com/MichaelBlume), [@gfredericks](https://github.com/gfredericks).

All not-good things can be attributed to me.

## Lineage

- Descendent of [Gary Frederick](https://github.com/gfredericks)'s [schema->gen gist](https://gist.github.com/gfredericks/9787803#)
- Forked from, originally, [Michael Blume](https://github.com/MichaelBlume)'s [repo](https://github.com/MichaelBlume/schema-gen)
- Shares/uses a ton of code from [Dave Golland](https://github.com/davegolland)'s [trial/gist](https://gist.github.com/davegolland/3bc4277fe109e7b11770) w/ test.check and Prismatic schema

## References

- [Prismatic schema](https://github.com/Prismatic/schema) - declarative data description 
- John Hughes's [keynote](https://www.youtube.com/watch?v=zi0rHwfiX1Q) at ClojureWest 2014
- Reid Draper's [test.check](https://github.com/clojure/test.check) [talk](https://www.youtube.com/watch?v=JMhNINPo__g) at ClojureWest 2014
- [schema-typer](https://github.com/circleci/schema-typer) - core.typed w/ Prismatic schema 
- [@philandstuff](https://twitter.com/philandstuff)'s [EuroClojure](http://euroclojure.com/2014/) [slides/talk](http://www.philandstuff.com/slides/2014/euroclojure.html#/)
- Multimethod approach shares ideas w/ [ring-swagger](https://github.com/metosin/ring-swagger)'s approach w/ Prismatic schema.

## License

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
