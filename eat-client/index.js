const express = require("express");
const app = express();
const bodyParser = require('body-parser');
const mealController = require('./meal_controller');
const authenticationController = require('./authentication_controller')
var path = require('path');

const port = 8080;

app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.urlencoded({ extended: true }));

app.get("/", (req, res) => {
    res.sendFile("index.html", { root: "./public" });
});

app.get("/register", (req, res) => {
    res.sendFile("register.html", { root: "./public" });
});

app.post("/", (req, res) => {
    let meal = mealController.addMeal(req.body);
    res.status = 200;
})

app.post('/register', (req, res) => {
    authenticationController.register(req.body);
    console.log(req.body);
    res.status = 200;
    res.sendFile("register.html", { root: "./public" });
});

app.listen(port, () => {
    console.log(`Listening on port: ${port}`);
});
