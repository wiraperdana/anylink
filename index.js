var vertx = require('vertx');
var console = require('vertx/console');
var container = require("vertx/container");
var logger = container.logger;

container.deployVerticle("AnyLinkRedisDAO.java");
container.deployVerticle("AnyLinkMysqlDAO.java");
container.deployVerticle("AnyLinkQuery.java");
container.deployVerticle("AnyLinkProcessor.java");

container.deployVerticle("post_server.js");
container.deployVerticle("get_server.js");
container.deployVerticle("net_server.js");

container.deployVerticle("post_client.js");
container.deployVerticle("get_client.js");
container.deployVerticle("net_client.js");