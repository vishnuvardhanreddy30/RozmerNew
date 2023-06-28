<script>
    import { createEventDispatcher, onMount } from "svelte";
    import Utils from "../../util/Utils.js";
    import Boot from "../../util/Boot";
    import proIcon from "../../assets/pro-nav-icon.png";
    import SessionUtil from "../../util/SessionUtil";
    import Labels from "../../const/Labels";
    import urlConst from "../../const/Url";
    import Request from "../../util/Request";

    let userInfo = {};
    let collapseIcon = "chevron_left";
    let myTotalFeed;
    
    $: {
        userInfo = SessionUtil.get("info", true);
    }
    const dispatch = createEventDispatcher();

    let isBigScreen = Boot.isBigScreen();

    function onSuccess(res) {
        myTotalFeed = (res['length'] < 2);
    }

    function onFailure(err) {
        Utils.log(err);
    }

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
        {
            text: Labels.menu.logout,
            icon: "logout",
            action: "logout",
        },
    ];

    export let selected;

    function onMenuItemClick(e) {
        let idx = +e.currentTarget.getAttribute("data-index");

        dispatch("showview", {
            view: menuItems[idx].action,
        });
    }

    function toggleSideMenu() {
        var me = this,
            sideMenuBar = document.getElementById('side-menu-cont'),
            isCollapseIconActive = (collapseIcon === "chevron_left");

        sideMenuBar.classList.toggle('toggle-menu');

        //Animate collapse left menu bar controller
        collapseIcon = isCollapseIconActive ?  "chevron_right" : "chevron_left";
        me.style.left = isCollapseIconActive ? "10px" : "230px";
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
    $: {
        if (selected) {
            setSelection();
        }
    }

    onMount(() => setSelection());
</script>
{#if !Boot.isMobile()}
        <span
            class="material-icons toggle-side-nav-btn nav-active"
            on:click={toggleSideMenu}>{collapseIcon}</span
        >
{/if}
<div class="side-menu flex-cont" id="side-menu-cont">
    {#if !isBigScreen}
        <div class="profile-logo-cont" />
    {/if}

    <div class="flex-cont flex-dir-column">
        <div class="pro-card-cont">
            <div align="center">
                <!-- svelte-ignore a11y-missing-attribute -->
                <img width="120px" src={proIcon} />
                <div>
                    <span class="pro-card-user-name"
                        >{userInfo.firstName} {userInfo.lastName}</span
                    >
                </div>
            </div>
        </div>
        {#each menuItems as item, idx}
            <div
                class="menu-item flex-cont {idx === 0 ? 'menu-selected' : ''}"
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

<style>
    .side-menu {
        min-width: 250px;
        justify-content: center;
        background-color: var(--white-color);
        border-right: 2px solid var(--border-nav);
        padding-top: 10px;
    }

    .side-menu .flex-dir-column {
        width: 100%;
    }

    .menu-item {
        padding: 1rem;
        align-items: center;
        cursor: pointer;
    }

    .menu-item-icon {
        color: var(--nav-icon-color);
        padding-right: 0.5rem;
        font-size: 1.2rem;
    }

    .menu-item-text {
        font-size: 0.9rem;
    }

    .pro-card-cont {
        padding: 10px 0;
        margin-bottom: 10px;
        border-bottom: 1px solid var(--border-nav);
    }

    .pro-card-cont img {
        margin: 10px auto;
    }

    .pro-card-user-name {
        font-weight: bold;
    }

    .pro-card-user-email {
        font-size: 0.8rem;
    }
</style>
