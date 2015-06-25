var http = require('http')
var bl = require('bl')

var d = [];
var r = 0;
get = function(index) {  
  http.get(process.argv[2 + index], function (response) {
    response.pipe(bl(function(err, data) {
      if (err)
        return console.error(err)
      d[index] = data.toString()
      if (++r >= 3) 
         for (var j = 0; j < 3; ++j) 
	    console.log(d[j]);
    }))
  })
}

for (var i = 0; i < 3; ++i) 
  get(i)
