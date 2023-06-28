// const base = '';

const base = 'http://localhost:9001/' // Enable it for Development

const urlConst = {
    register: base + 'add-user',
    verify_user: base + 'add-user/verify',

    // login
    login: base + 'add-user/login-user',

    //logout
    logout: base + '/add-user/users/logout',

    forgot_password: base + 'forgot_password',
    reset_pass: base + 'reset_password',

    // POST
    create_post: base + 'api/user/{userId}/posts',

    // Thumbnail update
    upload_post_thumbnail: base + 'api/post/image/upload/{postId}',

    // get thumbnail image - 'api/post/image/{imageName}'
    get_thumbnail_image: base + 'api/post/image/',

    // Get all post
    get_all_post: base + 'api/posts',

    // Search Post by title
    get_post_by_title: base + 'api/posts/search-by-title/{keywords}',

    // Get User POST
    get_user_posts: base + 'api/user/{userId}/posts',

    // Update a post
    update_post: base + 'api/posts/{postId}',

    // get post by post ID
    get_post_by_id: base + 'api/posts/{postId}',

    // delete a post by ID
    delete_post_by_id: base + 'api/posts/{postId}',

    // comments
    get_all_comments: base + 'api/comments/post/{postId}',
    get_comment_by_id: base + 'api/comments/post/{postId}/{commentId}',

    // post comments
    post_comments: base + 'api/post/{postId}/comments/user/{userId}',

    // delete comment
    delete_comment: base + 'api/comments/{commentId}',

    // Question
    post_question: base + 'ask-question/post/{postId}/question/user/{userId}',

    // get post questions
    get_post_questions: base + 'ask-question/question/{postId}',
    get_post_question_by_id: base + 'ask-question/question/{postId}/{qnId}',

    // delete comment
    delete_question: base + 'ask-question/question/{questionId}',

    // delete comment
    delete_answers: base + 'answer-question/answer/{answerId}',

    // Rating

    post_rating: base + 'api/pratings/post/{postId}/user/{userId}',

    // Comment Rating
    post_comment_rating: base + 'api/cratings/comments/{commentsId}/user/{userId}',

    // Question  Rating
    post_question_rating: base + 'api/qratings/questions/{questionId}/user/{userId}',

    // get rating
    get_rating: base + 'api/pratings/post/{postId}/user/{userId}',

    // get comment rating 
    get_comment_rating: base + '/api/cratings/comment/{commentId}/user/{userId}',

    // get question rating 
    get_question_rating: base + '/api/qratings/comment/{qnId}/user/{userId}',


    post_answer: base + 'answer-question/question/{questionId}/answer/user/{userId}',

    //update user profile
    post_user_profile: base + 'add-user/update-user/{userId}'
};

export default urlConst;