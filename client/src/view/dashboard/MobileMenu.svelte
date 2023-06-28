<script>
    import { createEventDispatcher, onMount } from "svelte";
    import Utils from "../../util/Utils";
    import Labels from "../../const/Labels";
    import urlConst from "../../const/Url";
    import Request from "../../util/Request";
    import SessionUtil from "../../util/SessionUtil";

    const dispatch = createEventDispatcher();
    let userInfo = {};
    let myTotalFeed;

    let menuItems = [
        {
            text: Labels.menu.home,
            icon: "menu_book",
            action: "home",
            selected: true,
        },
        /* {
            text: Labels.menu.notification,
            icon: "notifications",
            action: "notifications",
        },*/
        {
            text: Labels.menu.publish,
            icon: "publish",
            action: "publish",
        },
        {
            text: Labels.profile.my_post,
            icon: "dynamic_feed",
            action: "mypost",
        },
        {
            text: Labels.menu.profile,
            icon: "account_circle",
            action: "account",
        },
    ];

    export let selected;

    function onMenuItemClick(e) {
        let idx = +e.currentTarget.getAttribute("data-index");

        dispatch("showview", {
            view: menuItems[idx].action,
        });
    }

    function setSelection() {
        let items = document.querySelectorAll("[item-action]");

        for (let i = 0; i < items.length; i++) {
            items[i].classList.remove("menu-selected");
        }

        let item = document.querySelector(`[item-action=${selected}]`);

        if (!Utils.isEmpty(item)) {
            item.classList.add("menu-selected");
        }

        let url = urlConst.get_user_posts.replace("{userId}", userInfo.userId);
        Request.get(url, null, onSuccess, onFailure, onSuccess);
    }

    function onSuccess(res) {
        myTotalFeed = (res['length'] < 2);
    }

    function onFailure(err) {
        Utils.log(err);
    }

    $: {
        if (selected) {
            setSelection();
        }
        userInfo = SessionUtil.get("info", true);
    }

    onMount(() => setSelection());
</script>

<div class="mob-side-menu">
    <div class="flex-cont">
        {#each menuItems as item, idx}
            <div
                class="menu-item flex-cont flex-dir-column flex-1 {idx === 0
                    ? 'menu-selected'
                    : ''}"
                data-index={idx}
                on:click={onMenuItemClick}
                item-action={item.action}
            >
                <span class="material-icons menu-item-icon">{item.icon}</span>
                <span class="menu-item-text">{item.text}
                    {#if item.text === Labels.menu.publish && myTotalFeed} 
                        *
                    {/if}
                </span>
            </div>
        {/each}
    </div>
</div>
