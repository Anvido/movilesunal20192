const express = require('express')
const app = express()
require('./db')
const cats = require('./routes/cats')

app.get('/', (req, res) => res.send('Hello Ishkgur!!'))
app.use('/cats', cats)


app.listen(3000, () => console.log('Server on port 3000'))