const express = require("express");
const app = express();
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser')

const mealController = require('./meal_controller');
const authenticationController = require('./authentication_controller')

var path = require('path');

const port = 8080;

app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cookieParser());

app.get("/", (req, res) => {
    res.sendFile("index.html", { root: "./public" });
});

/**
 * Go to registration page
 */
app.get("/register", (req, res) => {
    res.sendFile("register.html", { root: "./public" });
});

/**
 * Go to login page
 */
app.get("/login", (req, res) => {
    res.sendFile("login.html", { root: "./public" });
});

app.post("/", (req, res) => {
    let meal = mealController.addMeal(req.body);
    res.status = 200;
})

/**
 * Create an accoutn and redirect to the correct page
 * if registered successfully, set cookie of JWT as well
 * 
 * TODO: error handling + correct redirect
 */
app.post('/register', (req, res) => {
    authenticationController.register(req.body).then(token => res
        .cookie("access_token", token, { httpOnly: true })
        .status(200)
        .redirect("/")
    );
});

/**
 * Login to your account and redirect to the correct page
 * if logged in successfully, set cookie of JWT as well
 * 
 * TODO: error handling + correct redirect
 */
app.post('/login', (req, res) => {
    authenticationController.authenticate(req.body).then(token => res
        .cookie("access_token", token, { httpOnly: true })
        .status(200)
        .redirect("/")
    );
});

app.listen(port, () => {
    console.log(`Listening on port: ${port}`);
});
