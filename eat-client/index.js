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

/**
 * Redirect to homepage
 */
app.get("/", (req, res) => {
    res.redirect("/home")
})

/**
 * Go to homepage
 */
app.get("/home", (req, res) => {
    const token = getToken(req)
    if (token) {
        res.sendFile("myhome.html", { root: "./public" });
    } else {
        res.sendFile("home.html", { root: "./public" });
    }
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

/**
 * Create an accoutn and redirect to the correct page
 * if registered successfully, set cookie of JWT as well
 * 
 * TODO: error handling
 */
app.post('/register', (req, res) => {
    return authenticationController.register(req.body).then(token => { 
        res.cookie("access_token", token, { httpOnly: true })
        res.redirect("/")
    });
});

/**
 * Login to your account and redirect to the correct page
 * if logged in successfully, set cookie of JWT as well
 * 
 * TODO: error handling
 */
app.post('/login', (req, res) => {
    return authenticationController.authenticate(req.body).then(token => { 
        res.cookie("access_token", token, { httpOnly: true })
        res.redirect("/")
    });
});

app.listen(port, () => {
    console.log(`Listening on port: ${port}`);
});

function getToken(request) {
    return request.cookies.access_token;
}
