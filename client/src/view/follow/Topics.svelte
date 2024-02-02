<script>
// @ts-nocheck
import { writable } from 'svelte/store';
    import { onMount, createEventDispatcher } from "svelte";
    import Utils from "../../util/Utils";
    // import Labels from "../../const/Labels";
    import urlConst from "../../const/Url";
    import Request from "../../util/Request";
    // import Button from "../../widget/button/Button.svelte";
    // import Boot from "../../util/Boot";
    import SessionUtil from "../../util/SessionUtil";
    import proIcon from "../../assets/user-icon.png";
    import axios from "axios";
    let userInfo = {};
    const activeTab = writable('users');
    let title = 'YOU ARE FOLLOWING'
    let follow_list = [
        'Technology',
        'Health',
        'Education'
    ]
    let usersList = []
    let title1 = "SEE MORE TOPICS"

    $: {
        userInfo = SessionUtil.get("info", true);
    }

    $: {
        fetchData('users')
    }

    function fetchData(tab) {
        usersList = []
        let url;
        if(tab === 'users') {
            url = urlConst.users_list.replace("{loginUserId}", userInfo.userId)
        }else if(tab==='followers') {
            url = urlConst.followers_list.replace("{loginUserId}", userInfo.userId)
        }else if(tab==='following') {
            url = urlConst.following_list.replace("{loginUserId}", userInfo.userId)
        }
        getUsersList(url)
    }

    function getUsersList(url) {
        axios.get(url, {
            headers: Request.getHeaders(null),
            params: null,
            timeout: 120000
        })
            .then(function (response) {
                let data = response.data;
                console.log("response : ", data)
                if(data.length > 0) {
                    usersList = data
                }

            })
            .catch(function (err) {
                Utils.log(err);
            });
    }

    function unFollowUser(user, index) {
        axios.get(urlConst.unfollow_user.replace("{followerId}", userInfo.userId).replace("{followingId}", user.userId), {
            headers: Request.getHeaders(null),
            params: null,
            timeout: 120000
        })
            .then(function (response) {
                let data = response.data;
                console.log("after unfollow : ", data)
                usersList[index].following = false;
            })
            .catch(function (err) {
                Utils.log(err);
            });

    }

    function followUser(user, index) {
        axios.get(urlConst.follow_user.replace("{followerId}", userInfo.userId).replace("{followingId}", user.userId), {
            headers: Request.getHeaders(null),
            params: null,
            timeout: 120000
        })
            .then(function (response) {
                let data = response.data;
                console.log("after follow : ", data)
                usersList[index].following = true;
            })
            .catch(function (err) {
                Utils.log(err);
            });
    }

</script>

<div class="feed-details col-12 mx-auto">
    <div class="">
        <div class="tab-buttons">
            <button on:click={() => {$activeTab = 'users'; fetchData('users')}} class:active={$activeTab === 'users'}>Users</button>
            <button on:click={() => {$activeTab = 'followers'; fetchData('followers')}} class:active={$activeTab === 'followers'}>Followers</button>
            <button on:click={() => {$activeTab = 'following'; fetchData('following')}} class:active={$activeTab === 'following'}>Following</button>
          </div>
        <!-- Added comments for following topics section -->
        <!-- <div class="mb-10">
            <span class="follow-title">{title}</span>
        </div>
        {#each follow_list as item, idx}
            <div class="flex-cont" data-index={idx}>
                <li class="follow-item">{item}</li>
            </div>
        {/each}

        <div class="border-b-1 mt-20"></div>
        <div class="flex-cont space-between mt-20">
            <span class="follow-title">{title1}</span>
            <span class="material-icons pointer"> add_box </span>
        </div> -->
        <div class="standard-bg border-radius-10 mt-10 p1">
            <div class="mb-10">
                <span class="whom-to-follow">WHOM TO FOLLOW</span>
            </div>
            {#each usersList as user,index (user.userId)}
                {#if user.userId !== userInfo.userId}
                <div class="d-flex flex-wrap" key={'follow'+user.userId}>
                    <img width="60px" height="60px" src={proIcon} alt="Profile Icon" />
                    <div class="user-info">
                        <span class="user-name">{user.firstName} {user.lastName}</span>
                        <span class="user-role">{user.email}</span>
                        {#if user.following}
                        <button class="follow-btn" on:click={() => unFollowUser(user, index)}>Following</button>
                        {:else}
                        <button class="follow-btn" on:click={() => followUser(user, index)}>Follow</button>
                        {/if}
                    </div>
                </div>
                {#if usersList.length-1 > index}
                <div class="border-b-2 mt-20 mb-20"></div>
                {/if}
                {/if}
            {/each}
        </div>
    </div>
</div>

<style>
.follow-title{
    font-size: .9rem;
    font-weight: 500;
    color: black;
    padding-bottom: 10px;
}
.follow-item{
    font-size: .8rem;
    font-weight: 500;
    color: black;
    margin-bottom: 5px;
}
.whom-to-follow{
    font-size: .9rem;
    font-weight: 500;
    color: white;
    padding-bottom: 10px;
}
.user-info{
    display: flex;
    flex-direction: column;
    padding: 0 10px;
}
.user-name{
    font-size: .8rem;
    font-weight: 500;
    color: white;
}
.user-role{
    font-size: .65rem;
    color: white;
    font-weight: 400;
}
.follow-btn{
    background-color: transparent;
    border: 2px solid white;
    color: white;
    font-size: .7rem;
    margin-top: 6px;
    border-radius: 16px;
    width: 100px;
    padding: 4px;
}
.tab-buttons {
    border-bottom: 1px solid #ccc;
    display: flex;
    margin-bottom: 30px;
  }

  .tab-buttons button {
    flex: 1;
    padding: 10px;
    cursor: pointer;
    border: none;
    outline: none;
    background-color: transparent;
    font-size: 16px;
    font-weight: 400;
    color: var(--primary-color-alternate-2); /* Adjust the color as needed */
  }

  .tab-buttons button.active {
    border-bottom: 3px solid var(--primary-color-alternate-2); /* Adjust the color as needed */
    font-weight: 700;
  }
</style>
