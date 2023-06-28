<script>
    import { onMount, createEventDispatcher } from "svelte";
    import BreadcrumbButton from "./BreadcrumbButton.svelte";

    export let data;
    export let separator = ">";

    let buttons = [];
    let buttonsRefs = {};

    const dispatch = createEventDispatcher();

    export function set(data) {
        let items = [],
            i,
            ln;
        data = [].concat(data);
        ln = data.length;

        for (i = 0; i < ln; i++) {
            items.push({
                id: "breadcrumb-btn-" + i,
                text: data[i].text || "",
                separator: i !== ln - 1 ? separator : null,
                data: data[i],
            });
        }

        buttons = items;
    }

    onMount(() => {
        set(data);
    });

    function onItemClick(e) {
        let data = e.detail,
            btnRef = buttonsRefs,
            destroy = false,
            newBtn = [];

        for (let key in btnRef) {
            if (destroy) {
                btnRef[key].$destroy();
            }

            if (key === data.id) {
                destroy = true;
                btnRef[key].separator = null;
            }
        }

        dispatch("itemclick", data);
    }
</script>

<div class="breadcrumb" class:hidden={!data || !data.length}>
    {#each buttons as item, idx}
        <BreadcrumbButton
            {...item}
            on:itemclick={onItemClick}
            bind:this={buttonsRefs["breadcrumb-btn-" + idx]}
        />
    {/each}
</div>

<style>
    .breadcrumb {
        background: #ddd;
    }
</style>
