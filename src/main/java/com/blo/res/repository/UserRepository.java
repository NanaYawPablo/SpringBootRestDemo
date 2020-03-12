package com.blo.res.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.blo.res.entity.User;

//@RepositoryRestResource(exported = false) //will not expose this repository

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{

	//findAll i.e. Iterables output
	public List<User> findAll();
	
	//count
	public long count();
	
	//findById
	User findById(int id);
	
	//findByUsername
	User findByUsername(String username);
	
	
//	@RestResource(exported = false) //won’t expose those HTTP methods that depend on this resource ie.prevent DELETE /users/:id
//	void deleteById(Long aLong);
	
	
//	 // custom query to search to blog post by title or content
//    List<Blog> findByTitleContainingOrContentContaining(String text, String textAgain);
//    
	
	//custom query method
	/** public List<Employee> getFirstNamesLike(String firstName) {
        Query query = entityManager.createNativeQuery("SELECT em.* FROM spring_data_jpa_example.employee as em " +
                "WHERE em.firstname LIKE ?", Employee.class);
        query.setParameter(1, firstName + "%");
        return query.getResultList();
    }
	 */
	
	//another custom query
	/*	
	@Query(
			  value = "SELECT * FROM USERS u WHERE u.status = 1", 
			  nativeQuery = true)
			Collection<User> findAllActiveUsersNative();*/
	
	
}
