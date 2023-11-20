<script setup>
/**
 * SFC for the soccer field to take skteches on.
 * @author daniel
 */
import { onMounted } from "vue";

onMounted(() => {
  const canvas = document.getElementById("penaltyArea");
  const wrapper = document.getElementById("soccerFieldContainer");
  drawSoccerField(canvas, wrapper);
});

function drawSoccerField(canvas, wrapper) {
  const ctx = canvas.getContext("2d");
  function drawPitch() {
    //Outer lines
    ctx.beginPath();
    //needed - otherwise canva is too big
    canvas.width = wrapper.clientWidth;
    canvas.height = wrapper.clientHeight;
    ctx.rect(0, 0, wrapper.clientWidth, canvas.height);
    ctx.fillStyle = "#060";
    ctx.fill();
    ctx.lineWidth = 1;
    ctx.strokeStyle = "#FFF";
    ctx.stroke();
    ctx.closePath();

    //Penalty box
    ctx.beginPath();
    ctx.rect(
        (wrapper.clientWidth / 2) * 0.5045,
        0,
        wrapper.clientWidth / 2,
        (wrapper.clientWidth / 2) * 0.4125
    );
    ctx.stroke();
    ctx.closePath();

    //Goal box
    ctx.beginPath();
    ctx.rect(
        (wrapper.clientWidth / 2) * 0.771,
        0,
        wrapper.clientWidth * 0.229,
        wrapper.clientWidth * 0.06875
    );
    ctx.stroke();
    ctx.closePath();

    //Goal
    ctx.beginPath();
    ctx.moveTo((wrapper.clientWidth / 2) * 0.9085, 2);
    ctx.lineTo(
        (wrapper.clientWidth / 2) * 0.9085 + wrapper.clientWidth * 0.0915,
        2
    );
    ctx.lineWidth = 9;
    ctx.stroke();
    ctx.closePath();

    //Penalty Point
    ctx.beginPath();
    ctx.arc(
        wrapper.clientWidth / 2,
        wrapper.clientWidth * 0.1375,
        3,
        0,
        2 * Math.PI,
        true
    );
    ctx.fillStyle = "#fff";
    ctx.fill();
    ctx.closePath();

    //Half Circle
    // Half Circle (Opens at the top)
    ctx.beginPath();
    const centerX = wrapper.clientWidth / 2;
    const centerY = (wrapper.clientWidth / 2) * 0.4125; // Adjust the value as needed
    const radius = wrapper.clientWidth * 0.08; // Adjust the value as needed
    const startAngle = 0; // Start from 0 degrees (top)
    const endAngle = Math.PI; // End at 180 degrees (bottom)
    ctx.arc(centerX, centerY, radius, startAngle, endAngle, false);
    ctx.lineWidth = 2;
    ctx.stroke();
    ctx.closePath();

    //Home L corner
    ctx.beginPath();
    ctx.arc(0, 0, 8, 0, 0.5 * Math.PI, false);
    ctx.stroke();
    ctx.closePath();
    //Home R corner
    ctx.beginPath();
    ctx.arc(wrapper.clientWidth, 0, 8, 0, 2 * Math.PI, true);
    ctx.stroke();
    ctx.closePath();
  }
  let isDrawing = false;

  canvas.addEventListener("mousedown", startDrawing);
  canvas.addEventListener("mouseup", stopDrawing);
  canvas.addEventListener("mousemove", draw);

  canvas.addEventListener("touchstart", startDrawing);
  canvas.addEventListener("touchend", stopDrawing);
  canvas.addEventListener("touchmove", draw);

  function startDrawing(e) {
    e.preventDefault();
    const rect = canvas.getBoundingClientRect();
    const x = e.clientX || e.touches[0].clientX;
    const y = e.clientY || e.touches[0].clientY;

    isDrawing = true;
    ctx.beginPath();
    ctx.strokeStyle = "#FFF";
    ctx.lineWidth = 3;
    ctx.imageSmoothingEnabled = false;
    ctx.moveTo(x - rect.left, y - rect.top);
  }

  function draw(e) {
    e.preventDefault();
    if (isDrawing) {
      const rect = canvas.getBoundingClientRect();
      const x = (e.clientX || e.touches[0].clientX) - rect.left;
      const y = (e.clientY || e.touches[0].clientY) - rect.top;

      ctx.lineTo(x, y);
      ctx.stroke();
    }
  }

  function stopDrawing() {
    isDrawing = false;
  }
  drawPitch();
}
</script>

<template>
  <div class="__soccer_field">
    <canvas id="penaltyArea" width="2000" height="400"></canvas>
  </div>
</template>

<style scoped>

</style>
