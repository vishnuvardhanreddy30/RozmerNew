<script>
    import { createEventDispatcher } from "svelte";
    import { fly } from "svelte/transition";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import TextField from "../../widget/fields/TextField.svelte";
    import Labels from "../../const/Labels";

    const dispatch = createEventDispatcher();

    let searchVal;

    function onBack() {
        dispatch("hidesearch");
    }

    function onSearchFeed() {
        dispatch("hidesearch");
        dispatch("mobilesearch", searchVal);
    }
</script>

<div
    transition:fly={{ x: window.innerWidth, duration: 500 }}
    class="search-cont"
>
    <Toolbar>
        <Button
            slot="left"
            iconCls="material-icons"
            iconText="keyboard_backspace"
            on:click={onBack}
            ui="action"
        />
        <div slot="center" class="flex-cont">
            <TextField
                placeholder={Labels.header.search}
                cls="search-feed"
                bind:value={searchVal}
                on:enter={onSearchFeed}
            />
            <Button
                iconCls="material-icons"
                iconText="search"
                on:click={onSearchFeed}
                ui="action"
            />
        </div>
    </Toolbar>
</div>

<style>
    .search-cont {
        position: fixed;
        width: 100%;
        background: var(--white-color);
    }
</style>
