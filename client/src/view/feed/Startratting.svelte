<script>
    export let rating = 0;
    
    function updateRating(value) {
      rating = value;
    }
  </script>
  
  <style>
    .star {
      fill: rgb(0, 147, 32);
      stroke: rgb(0, 73, 21);
      stroke-width: 1px;
    }
    
    .selected {
      fill: gold;
    }
  </style>
  
  <div>
    {#each Array(5) as _, i}
      <svg class="star" width="24" height="24" viewBox="0 0 24 24"
           on:mouseover={() => updateRating(i+1)}
           on:click={() => rating = i+1}>
        <polygon class={i < rating ? 'selected' : ''} points="12 2.276 15.364 8.618 22 9.864 17 14.236 18.182 21.618 12 18.618 5.818 21.618 7 14.236 2 9.864 8.636 8.618"/>
      </svg>
    {/each}
  </div>
  