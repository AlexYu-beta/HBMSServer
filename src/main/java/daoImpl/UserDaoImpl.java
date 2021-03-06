package daoImpl;
import dataHelper.DataFactory;
import dataHelper.UserDataHelper;
import dataHelperImpl.DBUtil_Alex;
import dataHelperImpl.DataFactoryImpl;
import dao.UserDao;
import message.ResultMessage;
import model.UserType;
import model.UserTypeHelper;
import po.UserPO;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
/**
 * Created by alex on 16-11-9.
 */
public class UserDaoImpl implements UserDao {
    Connection connection;
    private static UserPO userPO;
    private static UserDaoImpl userDaoImpl;
    private DataFactory dataFactory;
    private UserDataHelper userDataHelper;

    public UserDaoImpl() {
        if(userPO==null){
            dataFactory=new DataFactoryImpl();
            userDataHelper=dataFactory.getUserDataHelper();
            connection= DBUtil_Alex.getConnection();
        }
    }

    public static UserDaoImpl getInstance(){
        if(userDaoImpl==null){
            userDaoImpl=new UserDaoImpl();
        }
        return userDaoImpl;
    }

    @Override
    public UserPO getUserData(int id) throws RemoteException {
        return userDataHelper.getUserData(id);
    }

    @Override
    public ResultMessage addUser(UserPO po) throws RemoteException,Exception {
        return userDataHelper.addUser(po);
    }

    @Override
    public ResultMessage deleteUser(int id) throws RemoteException,Exception{
        return userDataHelper.deleteUser(id);
    }

    @Override
    public ResultMessage modifyUser(UserPO po) throws RemoteException,Exception{
        return userDataHelper.modifyUser(po);
    }

    @Override
    public ResultMessage login(int id, String pwd) throws RemoteException {
        userPO=userDataHelper.getUserData(id);
        if(userPO!=null&&pwd.equals(userPO.getPassword())){
            return ResultMessage.success;
        }else{
            userPO=null;
            return ResultMessage.failure;
        }
    }

    @Override
    public ResultMessage signup(UserPO po) throws RemoteException,Exception{
        String accountName=po.getAccountName();
        String sentence="select * from user where accountName='"+accountName+"'";
        PreparedStatement preparedStatement;
        try{
            preparedStatement = connection.prepareStatement(sentence);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.getString("userID")!=null){
                //a user with a same accountName already exists
                return ResultMessage.failure;
            }else{
                userDataHelper.addUser(po);
            }
        }catch(SQLException e){
            e.printStackTrace();
            return ResultMessage.failure;
        }
        return ResultMessage.success;
    }
}
