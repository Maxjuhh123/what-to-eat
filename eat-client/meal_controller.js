const axios = require('axios').default;

const URL = 'http://localhost:8081/meal';

function addMeal(meal) {
    let resultMeal = null;
    axios.post(URL, meal, {
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((res) => {
        resultMeal = res;
    }).catch((error) => {
        console.log(error);
    });
    return resultMeal
}

module.exports = { addMeal };