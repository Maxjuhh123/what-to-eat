const express = require("express");
const app = express();
const bodyParser = require('body-parser');
const mealController = require('./meal_controller');

const port = 8080;

app.use(express.static(__dirname + "/public"));
app.use(bodyParser.urlencoded({ extended: true }));

app.get("/", (req, res) => {
    res.sendFile("index.html", { root: "./public" });
});

app.post("/", (req, res) => {
    let meal = mealController.addMeal(req.body);
    res.send(meal);
})

app.listen(port, () => {
    console.log(`Listening on port: ${port}`);
});
