var async = require('async');
var xml4js = require('xml4js');
var util = require('util');
var fs = require('fs');
var _ = require('underscore');

var parser = new xml4js.Parser({downloadSchemas:false});

function loadSchema(namespace, schema, cb) {
   var content = fs.readFileSync("xsd/" + schema, {encoding: 'utf-8'});
   parser.addSchema(namespace, content, function (err, importsAndIncludes) {
     if (err) {
        cb(err);
     } else if (importsAndIncludes) {
	async.each(_.keys(importsAndIncludes), function (namespace, cb) {	
	  var schemas = importsAndIncludes[namespace];
	  if (!_.isArray(schemas)) {
    	    schemas = [schemas];
          }
	  async.each(schemas, function (schema, cb) {
	    loadSchema(namespace, schema, cb);
          }, cb);
        }, cb);
     } else
       cb(null);
   });
}

function loadSchemas(schemas, cb) {
  async.each(_.keys(schemas), function (namespace, cb) {
    var files = schemas[namespace];
    if (!_.isArray(files)) {
      files = [files];
    }
    async.each(files, function (file, cb) {
      loadSchema(namespace, file, cb);
    }, cb);
  }, cb);
}

/* xml4js does not work with RIF:CS 
 * The problem seems to be with XSD structure, used by ANDS
 * xml4js is not processing include directory correctly and 
 * does not loads data from registryTypes.xsd
 */

var schemas = {
  "http://ands.org.au/standards/rif-cs/registryObjects" : "registryObjects.xsd", 
  "http://www.openarchives.org/OAI/2.0/" : "OAI-PMH.xsd"
};

loadSchemas(schemas, function (err) {
  if (err) {
    console.log("Error: " + err);
  } else {
    var xml = fs.readFileSync('xml/0.xml', {encoding: 'utf-8'});
 
    parser.parseString(xml, function (err, result) {
      if (err) {
        console.log("Parse Error: " + err);	
      } else {
	console.log(util.inspect(result, false, null));
      }
    });
  }	
});

