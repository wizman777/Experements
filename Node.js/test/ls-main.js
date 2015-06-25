var ls = require('./ls-module.js')

ls(process.argv[2], process.argv[3], function (err, list) {
  list.forEach(function (file) {
    console.log(file)
  })
})
