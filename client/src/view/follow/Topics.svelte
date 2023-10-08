<script>
    import { onMount, createEventDispatcher } from "svelte";
    // import Utils from "../../util/Utils";
    // import Labels from "../../const/Labels";
    // import urlConst from "../../const/Url";
    // import Request from "../../util/Request";
    // import Button from "../../widget/button/Button.svelte";
    // import Boot from "../../util/Boot";
    // import SessionUtil from "../../util/SessionUtil";
    import proIcon from "../../assets/user-icon.png";

    let title = 'YOU ARE FOLLOWING'
    let follow_list = [
        'Technology',
        'Health',
        'Education'
    ]
    let title1 = "SEE MORE TOPICS"

    onMount(() => {
    });
</script>

<div class="feed-details col-12 mx-auto">
    <div class="">
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
            <div class="d-flex flex-wrap">
                <img width="60px" height="60px" src={proIcon} />
                <div class="user-info">
                    <span class="user-name">Vishnu Vardhan</span>
                    <span class="user-role">Software Engineer</span>
                    <button class="follow-btn">Follow</button>
                </div>
            </div>
            <div class="border-b-2 mt-20 mb-20"></div>
            <div class="d-flex flex-wrap">
                <img width="60px" height="60px" src={proIcon} />
                <div class="user-info">
                    <span class="user-name">Neeraj</span>
                    <span class="user-role">Software Engineer</span>
                    <button class="follow-btn">Follow</button>
                </div>
            </div>
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
</style>
