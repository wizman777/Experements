var fs = require('fs')
fs.readFile(process.argv[2], 'utf8', function done(err, content) {
    var lines = content.split('\n').length - 1
    console.log(lines)
})
