const express = require('express')
const router = express.Router()
const Cat = require('../models/cat')


router.get('/', (req, res) => {
    Cat.find((err, data) => {
        console.log(data)
    })
})

module.exports = router