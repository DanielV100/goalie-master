export function clearSessionStorage() {
    sessionStorage.clear();
}

export function getExercisesFromSessionStorage(category) {
    let exercises = [];
    if(category === 'warmUp') {
        exercises = JSON.parse(sessionStorage.getItem('exercisesWarmUp'));
    } else if(category === 'main') {
        exercises = JSON.parse(sessionStorage.getItem('exercisesMain'));
    } else if(category === 'end'){
        exercises = JSON.parse(sessionStorage.getItem('exercisesEnd'));
    }
    return exercises;
}

/**
 * Filters exercises by category group and saves them in session storage.
 * Saving in session storage is faster than requesting db.
 * @param exercises
 */
export function saveAllExercisesInSessionStorage(exercises) {
    let exerciseWarmUp = [];
    let exerciseMain = [];
    let exerciseEnd = [];
    let exerciseOther = [];
    exercises.forEach((exercise) => {
        if(exercise.categoryGroup.toLowerCase() === 'aufwärmen') {
            exerciseWarmUp.push(exercise);
        } else if(exercise.categoryGroup.toLowerCase() === 'hauptteil') {
            exerciseMain.push(exercise);
        } else if(exercise.categoryGroup.toLowerCase() === 'abschluss') {
            exerciseEnd.push(exercise);
        } else {
            exerciseOther.push(exercise);
        }
    });
    sessionStorage.setItem('exercisesWarmUp', JSON.stringify(exerciseWarmUp));
    sessionStorage.setItem('exercisesMain', JSON.stringify(exerciseMain));
    sessionStorage.setItem('exercisesEnd', JSON.stringify(exerciseEnd));
    sessionStorage.setItem('exercisesOther', JSON.stringify(exerciseOther));
}