<script setup>
import {onBeforeMount, onMounted, ref} from "vue";
import AddGoalkeeperForm from "@/components/_detailed/components/__addGoalkeeperForm/AddGoalkeeperForm.vue";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions.js";

const props = defineProps({
  entityId: Number,
  entityType: String
});
const entityToEdit = ref();

onBeforeMount(() => {
  entityToEdit.value = SessionStorageFunctions.getSessionStorageElementById(SessionStorageFunctions.getAllGoalkeepersFromSessionStorage(), props.entityId);
});
function init() {

}

</script>

<template>
  <div class="__entity_editor">
    <AddGoalkeeperForm v-if="props.entityType === 'goalkeeper'" :is-edit-view="true" :id="props.entityId" :firstname="entityToEdit.firstname" :lastname="entityToEdit.lastname" :birthday="entityToEdit.birthday" :club="entityToEdit.club" :notes="entityToEdit.notes"/>
  </div>
</template>

<style scoped>

</style>