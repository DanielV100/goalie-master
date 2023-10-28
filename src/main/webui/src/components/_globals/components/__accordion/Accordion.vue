<script setup>
import EditExerciseForm from "@/components/_detailed/components/__addExerciseForm/ViewExerciseForm.vue";
import {onMounted, ref} from "vue";
import ViewExerciseForm from "@/components/_detailed/components/__addExerciseForm/ViewExerciseForm.vue";
const exerciseIDs = ref([]);

function addExerciseButtonClicked() {
  exerciseIDs.value.push(prompt('Inout'));
}

</script>

<template>
  <div class="__accordion">
    <div class="table-container">
      <table>
        <tr>
          <td style="width: 50%">
            <label>Kategorie</label>
            <h2><slot></slot></h2>
          </td>
          <td class="after_td_one">
            <label>Dauer</label>
            <h2>00:00</h2>
          </td>
          <td class="after_td_one">
            <label>Intensität</label>
            <h2>2/5</h2>
          </td>
        </tr>
      </table>
      <hr>
      <div v-for="id in exerciseIDs" id="testArea">
        <ViewExerciseForm :exercise-i-d="id"></ViewExerciseForm>
      </div>
      <br>
      <hr>
      <button @click="addExerciseButtonClicked">+</button>
    </div>
  </div>
</template>

<style scoped>
.__accordion {
  width: 100%;
}

.table-container {
  overflow-x: auto;
}
.table-container table {
  width: 100%;
  border-collapse: collapse;
}
.after_td_one {
  padding-left: 20px;
}
.after_td_one h2 {
  color: var(--placeholderColor);
}
h2 {
  margin-top: 0;
}
button {
  width: 100%;
}


</style>
