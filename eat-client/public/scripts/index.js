document.getElementById("submitMealButton").onclick = mealSubmission;

function setSuccessMessage(message) {
    paragraph = document.getElementById("successMessage");
    paragraph.innerText = message;
}

function mealSubmission() {
    let meal = {
        "name": document.getElementById('mealName').innerText,
        "description": document.getElementById('mealDesc').innerText,
        "price": document.getElementById("mealPrice").innerText
    };
    //addMeal(meal);
}