/**
 * Central file for handling everything session storage related.
 * @importnat SessionStorage can only hold 5MB - keep that in mind.
 * @author daniel
 */
/**
 * Global clear of session strage.
 */
export function clearSessionStorage() {
    sessionStorage.clear();
}

/**
 * Gets all exercises from db and categorises them.
 * @param category
 * @returns {*[]}
 */
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
    let exercisesAll = [];
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
        exercisesAll.push(exercise);
    });
    sessionStorage.setItem('exercisesWarmUp', JSON.stringify(exerciseWarmUp));
    sessionStorage.setItem('exercisesMain', JSON.stringify(exerciseMain));
    sessionStorage.setItem('exercisesEnd', JSON.stringify(exerciseEnd));
    sessionStorage.setItem('exercisesOther', JSON.stringify(exerciseOther));
    sessionStorage.setItem('exercisesAll', JSON.stringify(exercisesAll));
}

export function getAllTrainingSessionFromSessionStorage() {
    return JSON.parse(sessionStorage.getItem('trainingSessionAll'));
}

export function saveAllTrainingSessionInSessionStorage(trainingSessions) {
    sessionStorage.setItem('trainingSessionAll', JSON.stringify(trainingSessions));
}

export function getAllExercisesFromSessionStorage() {
    return JSON.parse(sessionStorage.getItem('exercisesAll'));
}


export function saveAllGoalkeepersInSessionStorage(goalkeepers) {
    sessionStorage.setItem('goalkeepersAll', JSON.stringify(goalkeepers));
}

export function getAllGoalkeepersFromSessionStorage() {
    return JSON.parse(sessionStorage.getItem('goalkeepersAll'));
}

/**
 * Used for editing an entity.
 * @param obj
 * @param id
 * @returns {*}
 */
export function getSessionStorageElementById(obj, id) {
    for (const element of obj) {
        if (element.id === id) {
            return element;
        }
    }
}

export function getJwtTokenFromSessionStorage() {
    return sessionStorage.getItem('jwttoken');
}

export function setJwtTokenToLocalStorage(JwtToken) {
    sessionStorage.setItem('jwttoken', JwtToken);
}
