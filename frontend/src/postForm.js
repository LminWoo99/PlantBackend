import React, { useState } from 'react';
import axios from 'axios';

function PostForm() {
  const [tTitle, setTTitle] = useState('');
  const [tContent, setTContent] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = { tTitle, tContent };
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
        <label htmlFor="tTitle">tTitle:</label>
        <input type="text" id="tTitle" value={tTitle} onChange={(event) => setTTitle(event.target.value)} />
      </div>
      <div>
        <label htmlFor="tContent">tContent:</label>
        <textarea id="tContent" value={tContent} onChange={(event) => setTContent(event.target.value)} />
      </div>
      <button type="submit">Submit</button>
    </form>
  );
}

export default PostForm;
