<script setup>

/**** IMPORTS ****/
import {onBeforeMount, ref} from "vue";
/**** COMPONENTS ****/
import AddGoalkeeperForm from "@/components/_detailed/components/__goalkeeperForm/GoalkeeperForm.vue";
import AddExerciseForm from "@/components/_detailed/components/__exerciseForm/ExerciseForm.vue";
import CreateTrainingSessionForm from "@/components/_detailed/components/__trainingSessionForm/TrainingSessionForm.vue";
/**** UTILITY FUNCTIONS ****/
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions.js";

/**** VARIABLES ****/
const entityToEdit = ref();
const exercisesIdsWarmUp = ref([]);
const exercisesIdsMain = ref([]);
const exercisesIdsEnd = ref([]);
const goalkeepersIds = ref([]);

/**** PROPS ****/
const props = defineProps({
  entityId: Number,
  entityType: String
});

/**** HOOKS ****/
onBeforeMount(() => {
  if(props.entityType === 'goalkeeper') {
    entityToEdit.value = SessionStorageFunctions.getSessionStorageElementById(SessionStorageFunctions.getAllGoalkeepersFromSessionStorage(), props.entityId);
  } else if(props.entityType === 'exercise') {
    entityToEdit.value = SessionStorageFunctions.getSessionStorageElementById(SessionStorageFunctions.getAllExercisesFromSessionStorage(), props.entityId);
  } else if(props.entityType === 'training-session') {
    entityToEdit.value = SessionStorageFunctions.getSessionStorageElementById(SessionStorageFunctions.getAllTrainingSessionFromSessionStorage(), props.entityId);
    for (let exerciseElement of entityToEdit.value.exercises) {
      if(exerciseElement.categoryGroup.toLowerCase() === 'aufwärmen') {
        exercisesIdsWarmUp.value.push({exerciseId: exerciseElement.id, exerciseTitle: exerciseElement.title});
      } else if(exerciseElement.categoryGroup.toLowerCase() === 'hauptteil') {
        exercisesIdsMain.value.push({exerciseId: exerciseElement.id, exerciseTitle: exerciseElement.title});
      } else if(exerciseElement.categoryGroup.toLowerCase() === 'abschluss') {
        exercisesIdsEnd.value.push({exerciseId: exerciseElement.id, exerciseTitle: exerciseElement.title});
      }
    }
    for (let goalkeeperElement of entityToEdit.value.goalkeepers) {
      goalkeepersIds.value.push(goalkeeperElement.id);
    }
  }
});
</script>

<template>
  <div class="__entity_editor">
    <AddGoalkeeperForm v-if="props.entityType === 'goalkeeper'" :is-edit-view="true" :id="props.entityId" :firstname="entityToEdit.firstname" :lastname="entityToEdit.lastname" :birthday="entityToEdit.birthday" :club="entityToEdit.club" :notes="entityToEdit.notes"/>
    <AddExerciseForm v-if="props.entityType === 'exercise'" :is-edit-view="true" :id="props.entityId" :title="entityToEdit.title" :category="entityToEdit.category" :number-of-goalkeeper="entityToEdit.numberOfGoalkeepers" :duration="entityToEdit.duration" :intensity="entityToEdit.intensity" :note="entityToEdit.note" :materials="entityToEdit.materials" :numbers-of-material="entityToEdit.numbersOfMaterial" :description-steps="entityToEdit.descriptionSteps" :sketch="entityToEdit.sketch"/>
    <CreateTrainingSessionForm v-if="props.entityType === 'training-session'" :is-edit-view="true" :id="entityId" :title="entityToEdit.title" :date="entityToEdit.date" :note="entityToEdit.notes" :exercisesIdsWarmUp="exercisesIdsWarmUp" :exercisesIdsMain="exercisesIdsMain" :exercisesIdsEnd="exercisesIdsEnd" :goalkeepersIds="goalkeepersIds"/>
  </div>
</template>

<style scoped>

</style>