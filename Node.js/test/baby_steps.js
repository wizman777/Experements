var r = 0;
for (var i = 2; i < process.argv.length; ++i)
   r += +process.argv[i];
console.log(r)

