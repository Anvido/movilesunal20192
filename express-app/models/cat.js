const mongoose = require('../db')
const Cat = mongoose.model('Cat', { name: String })

//const kitty = new Cat({ name: 'Ishkgur' })
//kitty.save().then(() => console.log('meow'))

module.exports =  Cat