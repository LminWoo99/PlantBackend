import React, { useState } from 'react';
import axios from 'axios';

function PostForm() {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = { title, content };
    axios.post('/post', data)
      .then(response => {
        console.log(response);
        // 서버로부터 응답을 받은 후 처리할 로직
        // ...
      })
      .catch(error => {
        console.log(error);
        // 에러 처리 로직
        // ...
      });
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="title">Title:</label>
        <input type="text" id="title" value={title} onChange={(event) => setTitle(event.target.value)} />
      </div>
      <div>
        <label htmlFor="content">Content:</label>
        <textarea id="content" value={content} onChange={(event) => setContent(event.target.value)} />
      </div>
      <button type="submit">Submit</button>
    </form>
  );
}

export default PostForm;
