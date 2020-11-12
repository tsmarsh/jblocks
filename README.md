# JBlocks

## tl;dr;

If your application is about taking json, validating it then transforming it and then passing it on to something else, you might be writing too much code. 

[JSONSchema](https://json-schema.org) is an excellent and fast DSL for describing how your json should look in JSON itself.
[JQ](https://stedolan.github.io/jq/) is an excellent and fast DSL for transforming your json into a different shapes. 

The purpose of this project is to demonstrate that, using kubernetes, you can string together these operations to form the core of your application leaving you with just the custom logic.

## TODO

1. ~~A [micro service](schemablock) that validates your json~~
2. ~~A [micro service](schemapass) that validates your json~and passes the results to the next service~~
3. ~~A [micro service](queryblock) that translates your json~~
4. ~~A [micro service](querypass) that translates your json~and passes the results to the next service~~
5. ~~A [cluster](k8s) that demonstrates a simple application~~
6. Tees that direct your json based on validation or translation success.
7. Blocks that pull data from restful services
8. Blocks that pull data from graphql services
9. Blocks that store data in document stores
10. Blocks that query data from document stores
11. Aggregation blocks to pull data from multiple blocks into a single document