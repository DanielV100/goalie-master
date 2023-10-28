<script setup>
import * as LocalConfig from './resources/addExerciseFormConfig.js';
import * as UtilityFunctions from '../../../../globals/utilityFunctions.js';
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import SoccerField from "@/components/_globals/components/__soccerField/SoccerField.vue";
import {computed, onBeforeMount, onMounted, ref} from "vue";
import SuccessAnimation from "@/components/_globals/components/__successAnimation/SuccessAnimation.vue";

//ToDo: Wie kann ich die Felder für die Materialien dynamsich erzeugen?
//--> V-for mit const ref materialList (leider funktioniert dies noch nicht!)



const isNotSubmitted = ref(true);
const title = ref('');
const category = ref('');
const numberOfGoalkeeper = ref('');
const duration = ref('');
const intensity = ref('');
const materialCount = ref('');
const material = ref('');



const materialList = ref({
  "2" : "Stangen",
  "9" : "Hütchen",
  "1" : "Bälle",
  "1" : "Tore"
});


const descriptionList = ref([
    'This is a description',
    'Lorem ipsum'
]);

let i = 0;
const props = defineProps({
  title: String,
  category: String,
  numberOfGoalkeeper: String,
  duration: String,
  intensity: String,
  material: String,
  materialCount: String,
  testArray:Object,
  exerciseID:String
});

onMounted(() => {
  title.value = props.title;
  category.value = props.category;
  numberOfGoalkeeper.value = props.numberOfGoalkeeper;
  duration.value = props.duration;
  intensity.value = props.intensity;
  material.value = props.material;
  materialCount.value = props.materialCount;
  materialList.value = props.testArray;
test();
});


function test() {

  const materialElements = document.querySelectorAll('#material_element');
  console.log(materialElements.length);
  if(materialElements.length === 3) {
    for (let valueKey in materialList.value) {
      const materialElement = document.getElementById('material_element');
      const clone = materialElement.cloneNode(true);
      const inputs = clone.querySelectorAll('.input');
      inputs.forEach((input) => (input.value = ''));
      document.getElementById('material_container').appendChild(clone);
    }
  }
}
</script>

<template>
  <div class="__edit_exercise_form">
    <div v-if="isNotSubmitted" class="wrapper_edited">
      <form class="form">
        <div class="column">
          <div class="input-box">
            <slot></slot>
            <label>{{ LocalConfig.FORM_TITLE_LABEL }}</label>
            <input
                :disabled="true"
                v-model="title"
                id="firstname"
                type="text"
                placeholder="Hechten II"
                required
            />
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_CATEGORY_LABEL }}</label>
            <select :disabled="true" v-model="category" name="cars" id="cars">
              <option value="others">Sonstige</option>
              <optgroup label="Aufwärmen">
                <option value="coordination">{{ LocalConfig.CATEGORY_WARM_UP_COORDINATION }}</option>
                <option value="balance_ability">{{ LocalConfig.CATEGORY_WARM_UP_BALANCE_ABILITY }}</option>
                <option value="balance_ability">{{ LocalConfig.CATEGORY_WARM_UP_JUMP_COORDINATION }}</option>
              </optgroup>
              <optgroup label="Hauptteil">
                <option value="1vs1">{{ LocalConfig.CATEGORY_MAIN_1vs1 }}</option>
                <option value="pike">{{ LocalConfig.CATEGORY_MAIN_PIKE }}</option>
              </optgroup>
              <optgroup label="Abschluss">
                <option value="mercedes">Mercedes</option>
                <option value="audi">Audi</option>
              </optgroup>
            </select>
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_NUMBER_OF_GOALKEEPER_LABEL }}</label>
            <input
                :disabled="true"
                v-model="numberOfGoalkeeper"
                type="number"
                placeholder="2"
                required
            />
          </div>
        </div>
        <div class="column">
          <div class="input-box">
            <label>{{ LocalConfig.FORM_DURATION_LABEL }}</label>
            <input
                :disabled="true"
                v-model="duration"
                onfocus="(this.type = 'time')"
                id="lastname"
                type="text"
                placeholder="00:15"
                required
            />
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_INTENSITY_LABEL }}</label>
            <input
                :disabled="true"
                v-model="intensity"
                type="number"
                min="0"
                max="5"
                placeholder="0 (gering) - 5 (hoch)"
                required
            />
          </div>
          <div class="input-box" id="multi">
            <div id="material_container">
              <label style="width: 100%">{{ LocalConfig.FORM_MATERIAL_LABEL }}</label>
              <div v-for="material in materialList" id="material_element" style="display: flex">
                <input :disabled="true" :value="material" class="input" type="number" style="width: 25%; margin-right: 8px" placeholder="9" />
                <input
                    :disabled="true"
                    :value="materialList[material]"
                    class="input"
                    type="text"
                    style="width: 75%;"
                    placeholder="Stangen"
                    list="materials"
                    required
                />
              </div>
            </div>
          </div>
        </div>
        <br />
      </form>
      <div id="description_container">
        <form @submit="onSubmitClick" class="form" id="form">
          <label>{{ LocalConfig.FORM_DESCRIPTION_LABEL }}</label>
          <div v-for="description in descriptionList" id="description" style="display: flex">
            <textarea :value="description" class="input" id="textTest" rows="2"></textarea>
          </div>
        </form>
      </div>
      <br>
      <br>
      <div>
        <img src="data:image/png;base64,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"/>
      </div>
      <br>
      <div>
        <br>
        <label>{{ LocalConfig.FORM_NOTES_LABEL }}</label>
        <textarea class="input" id="textTest" rows="3"></textarea>
      </div>
    </div>
    <SuccessAnimation v-else>{{ LocalConfig.SUCCESS_MESSAGE }}</SuccessAnimation>
  </div>
</template>

<style scoped>
.__edit_exercise_form {
  display: grid;
}
.wrapper_edited {
  place-self: center;
  min-width: 100%;
  max-width: 100%;
  background: var(--lightGrey);
  text-align: left;
}
.form :where(.input-box input) {
  position: relative;
  height: 50px;
  width: 100%;
  margin-top: 8px;
}
img {
  width: 100%;
}
</style>