var fs = require('fs')
var lines = fs.readFileSync(process.argv[2]).toString().split('\n')
console.log(lines.length - 1);
 
