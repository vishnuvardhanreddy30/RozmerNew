<script>
    import Utils from "../../util/Utils";
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";
    import SessionUtil from "../../util/SessionUtil";
    import Base from "../../util/Base";
    import likeIcon from "../../assets/rating/like.png";
    import dislikeIcon from "../../assets/rating/dislike.png";
    import awesomeIcon from "../../assets/rating/ok.png";

    export let postId = "";
    export let postUserId = null;
    export let details;

    let value = 0;
    let rating = 0;
    let role = SessionUtil.get("info", true).role;
    let valueDisplayed = "Cool";
    let valueIcon = likeIcon;
console.log("details in ratings : ", details, details.rating)
rating = details.rating
    /**
     * 0 --> Post
     * 1 --> Comment
     * 2 --> Question
     * 3 --> Answer
     */
    // let type = 0;

    function submitRating(value) {
        if(value === '-5') {
            Base.createRatingPopup({
                callback: function(action, comment) {
                    Utils.hideAlert();
                    
                    // 1: takedown
                    if(action) {
                        submitRatingConfirm(value, 1, comment);
                    } else {
                        submitRatingConfirm(value);
                    }
                }
            });
            return;
        }

        submitRatingConfirm(value);
    }

    function submitRatingConfirm(value, takeDown, comment) {
        Request.post(
            urlConst.post_rating
                .replace("{postId}", postId)
                .replace("{userId}", SessionUtil.get("info", true).userId),
            {
                rating: value,
                takeDown: takeDown || null,
                comment: comment || null
            },
            (res) => {
                updateValueDisplayed(res.rating);
                Utils.log(res);
            },
            (err) => {
                Utils.log(err);
            },
            submitRating
        );
    }

    function updateValueDisplayed(value) {
        switch (true) {
            case value > 0:
                valueDisplayed = "Awesome";
                valueIcon = awesomeIcon;
                break;
            case value < 0:
                valueDisplayed = "Not Cool";
                valueIcon = dislikeIcon;
                break;
            default:
                valueDisplayed = "Cool";
                valueIcon = likeIcon;
        } 
    }

    function onValueChange(e) {
        if(role === 'guest'){
            Utils.showNotification('You should signup to access this screen (or) functionality')
            return
        }
        value = e.target.value;
    
        submitRating(value);
    }

    $: {
        Request.get(
            urlConst.get_rating
                .replace("{postId}", postId)
                .replace("{userId}", SessionUtil.get("info", true).userId),
            null,
            (res) => {
                let pRating = (res.pratingGetDto[0] && res.pratingGetDto[0].rating) || 0;
                console.log("prate : ", pRating)
                value = pRating;
                rating = pRating
                updateValueDisplayed(pRating);
                Utils.log(res);
            },
            (err) => {
                Utils.log(err);
            },
            submitRating
        );
    }
 

  /**
     * @param {number} value
     */
  function handleRatingClick(value) {
    if(role === 'guest'){
            Utils.showNotification('You should signup to access this screen (or) functionality')
            return
        }
    rating = value;
    submitRating(rating);
    console.log("after rading updated : ", rating)
  }
</script>

<div class="rating-container wh-100-percent">
    <!-- Rating: {value} -->
    <div class="points-value-cont">
        <span class="points"> 
            <!-- <b>{valueDisplayed} 
                <img class="icon-cont" alt="" width="13px" src={valueIcon} />
            </b> -->
            <b>Average Rating</b>
        </span>
    </div>
    <div class="text-center">
        {#each [1, 2, 3, 4, 5] as value}
          <span on:click={() => handleRatingClick(value)} class="star">{rating >= value ? '★' : '☆'}</span>
        {/each}
      </div>
      
    <!-- <input
        type="range"
        min="-5"
        max="5"
        {value}
        class="slider"
        on:change={onValueChange}
    /> -->
    <!-- <div class="slider-values">
        <span>Not Cool <img class="icon-cont" alt="" width="13px" src={dislikeIcon} /></span>
        <span>Cool <img class="icon-cont" alt="" width="13px" src={likeIcon} /></span>
        <span>Awesome <img class="icon-cont" alt="" width="13px" src={awesomeIcon} /></span>
    </div> -->
</div>

<style>
    .rating-container label {
        font-size: 0.8em;
    }

    .icon-cont {
        vertical-align: inherit;
    }

    .points-value-cont {
        margin-top: 10px;
        text-align: center;
    }
    .points{
        font-size: 20px;
    }
    .star {
        color: gold;
        cursor: pointer;
        font-size: 40px;
    }
</style>
