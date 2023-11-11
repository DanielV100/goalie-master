export const APP_NAME = "G⚽alieMaster";

/**
 * These are the same routes as in App.vue.
 * Why? Needed for changing windows and components name never changes,
 * but paths could change.
 *
 */
const ROUTES_MAP = {
    'LoginForm':'#/',
    'Overview':'#/menu',
    'AddGoalkeeperForm':'#/add-goalkeeper',
    'AddExerciseForm':'#/add-exercise',
    'CreateTrainingSessionForm':'#/create-training-session',
    'GeneralOverview':'#/general-overview'
};

/**
 * Changes browser window to target route.
 * @param targetComponent
 */
export function changeWindowToTargetRoute(targetComponent) {
    window.location = ROUTES_MAP[targetComponent];
}
