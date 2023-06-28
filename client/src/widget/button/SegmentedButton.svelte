<script>
    import {createEventDispatcher} from "svelte"
    import Button from "./Button.svelte";
    import Utils from "../../util/Utils";

    const dispatch = createEventDispatcher();
    export let btnCls = '';

    export let buttons = [];

    $: {
        if(!Utils.isEmpty(buttons)) {
            for(let i = 0; i < buttons.length; i++){
                let btn = buttons[i];
                let cls = btn.cls || '';

                cls += ' ' + btnCls + ' segmented-btn';

                btn.cls = cls;

                btn.itemId = i;
            }
        }
    }

    function onClick(e) {
        for(let i = 0; i < buttons.length; i++) {
            buttons[i].pressed = false;
        }

        let recId = e.currentTarget.getAttribute('rec-id');

        buttons[+recId].pressed = true;

        dispatch('select', buttons[+recId]);
    }
</script>

<div class="segmented-btn-cont flex-cont">
    {#if !Utils.isEmpty(buttons)}
         {#each buttons as button, idx}
            <Button {...button} on:click={onClick}></Button>
         {/each}
    {/if}
</div>


