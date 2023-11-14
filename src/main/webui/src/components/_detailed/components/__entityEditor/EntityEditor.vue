<script setup>
import {onBeforeMount, onMounted, ref} from "vue";
import AddGoalkeeperForm from "@/components/_detailed/components/__addGoalkeeperForm/AddGoalkeeperForm.vue";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions.js";
import AddExerciseForm from "@/components/_detailed/components/__addExerciseForm/AddExerciseForm.vue";

const props = defineProps({
  entityId: Number,
  entityType: String
});
const entityToEdit = ref();

onBeforeMount(() => {
  if(props.entityType === 'goalkeeper') {
    entityToEdit.value = SessionStorageFunctions.getSessionStorageElementById(SessionStorageFunctions.getAllGoalkeepersFromSessionStorage(), props.entityId);
  } else if(props.entityType === 'exercise') {
    entityToEdit.value = SessionStorageFunctions.getSessionStorageElementById(SessionStorageFunctions.getAllExercisesFromSessionStorage(), props.entityId);
  }
});
function init() {

}

</script>

<template>
  <div class="__entity_editor">
    <AddGoalkeeperForm v-if="props.entityType === 'goalkeeper'" :is-edit-view="true" :id="props.entityId" :firstname="entityToEdit.firstname" :lastname="entityToEdit.lastname" :birthday="entityToEdit.birthday" :club="entityToEdit.club" :notes="entityToEdit.notes"/>
    <AddExerciseForm v-if="props.entityType === 'exercise'" :is-edit-view="true" :id="props.entityId" :title="entityToEdit.title" :category="entityToEdit.category" :number-of-goalkeeper="entityToEdit.numberOfGoalkeepers" :duration="entityToEdit.duration" :intensity="entityToEdit.intensity" :note="entityToEdit.note" :materials="entityToEdit.materials" :numbers-of-material="entityToEdit.numbersOfMaterial" :description-steps="entityToEdit.descriptionSteps" :sketch="entityToEdit.sketch"/>
  </div>
</template>

<style scoped>

</style>