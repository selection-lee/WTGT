<template>
    <div class="flex h-full">
      <div class="flex-1 flex flex-col h-full">
        <h2 class="text-xl font-bold mb-4">Simulator</h2>
        <div class="flex-1 rounded-lg overflow-hidden">
          <img 
            src="@/assets/unity.png" 
            alt="Unity Simulator" 
            class="w-full h-full object-cover"
          />
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "SimulatorView",
  };
  </script>